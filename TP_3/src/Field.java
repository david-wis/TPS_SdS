import java.util.*;

public class Field {
    private final float L;
    private List<Particle> particles;
    private Obstacle obstacle;
    private TreeMap<Float, List<Event>> events;
    private HashMap<Particle, List<Float>> eventTimesByParticle;

    public Field(float l, List<Particle> particles, Obstacle obstacle) {
        L = l;
        this.particles = particles;
        this.obstacle = obstacle;
    }

    private void init() {
        events = new TreeMap<>();
        eventTimesByParticle = new HashMap<>();
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            float tWall = p.timeToHitWall(L);
            events.putIfAbsent(tWall, new ArrayList<>());
            events.get(tWall).add(new WallCollisionEvent(p, L));
            eventTimesByParticle.putIfAbsent(p, new ArrayList<>());
            eventTimesByParticle.get(p).add(tWall);

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
//            events.add(new ObstacleCollisionEvent(p, obstacle));
        }
    }

    private void updateAssociatedTimes(Particle p, float universalTime) {
        for (float t : eventTimesByParticle.get(p)) {
            if (events.containsKey(t))
                events.get(t).removeIf(e -> e.particleInEvent(p));
        }
        eventTimesByParticle.get(p).clear();
        for (Particle q : particles) {
            if (p.equals(q)) continue;

            float tParticle = p.timeToHitParticle(q) + universalTime;
            if (tParticle == Float.POSITIVE_INFINITY)
                continue;
            events.putIfAbsent(tParticle, new ArrayList<>());
            events.get(tParticle).add(new ParticleCollisionEvent(p,q));
            eventTimesByParticle.get(p).add(tParticle);
            eventTimesByParticle.get(q).add(tParticle);
        }
    }

    public void loop(float duration) {
        init();
        FileController.writeParticlesState("output/state.txt", particles, false);

        int count = 0;
        float universalTime = 0;
        while (universalTime < duration) {
            count++;
//            System.out.println("Iterations:" + count++);
//            System.out.println(universalTime + ": " + particles.get(0).getX() + ", " + particles.get(0).getY() + ", " + particles.get(0).getVx() + ", " + particles.get(0).getVy() + ", " +
//                    particles.get(1).getX() + ", " + particles.get(1).getY() + ", " + particles.get(1).getVx() + ", " + particles.get(1).getVy());



            // print speed of particles
//            float totalSpeed = 0;
//            float totalSpeedSquared = 0;
//            for (Particle p : particles) {
//                System.out.println("Speed of particle " + p.getId() + ": " + Math.sqrt(p.getVx() * p.getVx() + p.getVy() * p.getVy()));
//                totalSpeed += (float) Math.sqrt(p.getVx() * p.getVx() + p.getVy() * p.getVy());
//                totalSpeedSquared += p.getVx() * p.getVx() + p.getVy() * p.getVy();
//            }
//            System.out.println("Total speed: " + totalSpeed);
//            System.out.println("Total speed squared: " + totalSpeedSquared);


            if (universalTime < 0) {
                throw new IllegalStateException("Time cannot be negative");
            }
            Map.Entry<Float, List<Event>> entry = events.pollFirstEntry();
            float t = entry.getKey();
            float dt = t - universalTime;
            particles.forEach(p -> p.updatePosition(dt, L));
            Set<Particle> allAffectedParticles = new HashSet<>();
            for (Event event : entry.getValue()) {
//                System.out.println(event.getClass().getName());
                if (event instanceof ParticleCollisionEvent) {
                    System.out.println("Particle collision: " + count + " between " + event.getParticles().get(0).getId() + " and " + event.getParticles().get(1).getId());
                } else {
                    System.out.println("Wall collision: " + count);
                }

                List<Particle> affectedParticles = event.execute(dt);
                allAffectedParticles.addAll(affectedParticles);
                for (Particle p : affectedParticles) {
                    float tToHit = p.timeToHitWall(L);
                    float newt = tToHit + t;
                    events.putIfAbsent(newt, new ArrayList<>());
                    events.get(newt).add(new WallCollisionEvent(p, L));
                    updateAssociatedTimes(p, universalTime);

                }
            }

            FileController.writeParticlesState("output/state.txt", particles, allAffectedParticles, true); // TODO: indicate which particles/walls are colliding
            universalTime = t;
//            System.out.println(universalTime);
        }
    }

}
