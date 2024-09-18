import java.util.*;

public class Field {
    private final float L;
    private final float V;
    private final String BASE_PATH;
    private List<Particle> particles;
    private Obstacle obstacle;
    private TreeMap<Float, List<Event>> events;
    private HashMap<Particle, List<Float>> eventTimesByParticle;

    private TreeMap<Float, List<Event>> wallEvents = new TreeMap<>();
    private TreeMap<Float, List<Event>> obstacleEvents = new TreeMap<>();

    private List<Particle> movingObstaclePositions = new ArrayList<>();

    public Field(float l, float v, List<Particle> particles, Obstacle obstacle) {
        L = l;
        V = v;
        this.particles = particles;
        this.obstacle = obstacle;
        BASE_PATH = (obstacle == null)? "output/moving/" : "output/" + (int) v + "/" ;
    }

    private void init() {
        events = new TreeMap<>();
        eventTimesByParticle = new HashMap<>();
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);

            eventTimesByParticle.put(p, new ArrayList<>());
            loadTimeToHitWall(p, 0);
            loadTimeToHitObstacle(p, 0);

            for (int j = i+1; j < particles.size(); j++) {
                Particle q = particles.get(j);
                float tParticle = p.timeToHitParticle(q);
                if (tParticle == Float.POSITIVE_INFINITY)
                    continue;
                events.putIfAbsent(tParticle, new ArrayList<>());
                events.get(tParticle).add(new ParticleCollisionEvent(p, q));

                eventTimesByParticle.get(p).add(tParticle);

                eventTimesByParticle.putIfAbsent(q, new ArrayList<>());
                eventTimesByParticle.get(q).add(tParticle);
            }
        }
    }

    private void updateAssociatedTimes(Particle p, float currentTime) {
        for (float t : eventTimesByParticle.get(p)) {
            if (events.containsKey(t)) {
                events.get(t).removeIf(e -> e.particleInEvent(p));
                if (events.get(t).isEmpty())
                    events.remove(t);
            }
        }
        eventTimesByParticle.get(p).clear();
        for (Particle q : particles) {
            if (p.equals(q)) continue;

            float tParticle = p.timeToHitParticle(q);
            if (tParticle == Float.POSITIVE_INFINITY)
                continue;
            tParticle += currentTime;
            events.putIfAbsent(tParticle, new ArrayList<>());
            events.get(tParticle).add(new ParticleCollisionEvent(p,q));
            eventTimesByParticle.get(p).add(tParticle);
            eventTimesByParticle.get(q).add(tParticle);
        }
        loadTimeToHitObstacle(p, currentTime);
        loadTimeToHitWall(p, currentTime);
    }

    private void loadTimeToHitObstacle(Particle p, float currentTime) {
        if (obstacle == null) return;

        float tToHitO = p.timeToHitObstacle(obstacle);
        if (tToHitO != Float.POSITIVE_INFINITY) {
            float newt = tToHitO + currentTime;
            events.putIfAbsent(newt, new ArrayList<>());
            events.get(newt).add(new ObstacleCollisionEvent(p, obstacle));
            eventTimesByParticle.get(p).add(newt);
        }
    }

    private void loadTimeToHitWall(Particle p, float currentTime) {
        Particle.WallHit hit = p.timeToHitWall(L);
        float tToHitW = hit.dt();

        float newt = tToHitW + currentTime;
        events.putIfAbsent(newt, new ArrayList<>());
        events.get(newt).add(new WallCollisionEvent(p, L, hit.ws()));
        eventTimesByParticle.get(p).add(newt);
    }

    private void registerEvent(Event e, float time) {
        if (e instanceof WallCollisionEvent) {
            wallEvents.putIfAbsent(time, new ArrayList<>());
            wallEvents.get(time).add(e);
        } else if (e instanceof ObstacleCollisionEvent) {
            obstacleEvents.putIfAbsent(time, new ArrayList<>());
            obstacleEvents.get(time).add(e);
        }
    }

    public void loop(float duration, float interval) {
        final String stateDir = BASE_PATH + "state_" + (int) V + ".txt";
        final String movObsDir = BASE_PATH + "moving_obstacle_positions_" + (int) V + ".txt";
        final String wallDir = BASE_PATH + "wall_events_" + (int) V + ".txt";
        final String obsDir = BASE_PATH + "obstacle_events_" + (int) V + ".txt";
        init();

        TreeMap<Float, Particle> movingObstaclePositions = new TreeMap<>();
        movingObstaclePositions.put(0f, particles.get(0).clone());

        FileController.writeParticlesState(stateDir, particles, false);
        FileController.writeObstaclePositions(movObsDir, movingObstaclePositions, false);
        FileController.createEmptyFile(wallDir);
        FileController.createEmptyFile(obsDir);

        int count = 0;
        int writes = 0;
        float universalTime = 0;
        float lastSnapshotTime = 0;

        while (universalTime < duration) {

            if (universalTime < 0) {
                throw new IllegalStateException("Time cannot be negative");
            }
            Map.Entry<Float, List<Event>> entry = events.pollFirstEntry();
            float currentTime = entry.getKey();
            float dt = currentTime - universalTime;
            particles.forEach(p -> p.updatePosition(dt, L));
            Set<Particle> allAffectedParticles = new HashSet<>();
            for (Event event : entry.getValue()) {
//                if (dt == 0) {
//                    System.out.println("Time is zero " + event.getClass().getName() + " universal time: " + universalTime + " current time: " + currentTime);
//                }
                registerEvent(event, currentTime);

                allAffectedParticles.addAll(event.execute(dt));
            }
            for (Particle p : allAffectedParticles) {
                updateAssociatedTimes(p, currentTime);
            }
//            System.out.println("Iterations:" + count + " Time: " + universalTime);
            if (obstacle == null) {
                movingObstaclePositions.put(currentTime, particles.get(0).clone());
            }

            if (universalTime > lastSnapshotTime + interval) {
                FileController.writeParticlesState(stateDir, particles, allAffectedParticles, true);
                FileController.writeEvent(wallDir, wallEvents);
                FileController.writeEvent(obsDir, obstacleEvents);
                wallEvents.clear();
                obstacleEvents.clear();
                if (obstacle == null) {
                    FileController.writeObstaclePositions(movObsDir, movingObstaclePositions, true);
                    movingObstaclePositions.clear();
                }
                lastSnapshotTime += interval;
                writes++;
                System.out.println("iterations: " + count + " writes: " + writes + " time: " + universalTime);
            }
            universalTime = currentTime;
            count++;


        }
        System.out.println("universal time: " + universalTime);
    }

}
