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

        float universalTime = 0;
        while (universalTime < duration) {
            System.out.println(universalTime + ": " + particles.get(0).getX() + " " + particles.get(0).getY() + " " + particles.get(0).getVx() + " " + particles.get(0).getVy() + " " +
                    particles.get(1).getX() + " " + particles.get(1).getY() + " " + particles.get(1).getVx() + " " + particles.get(1).getVy());
            if (universalTime < 0) {
                throw new IllegalStateException("Time cannot be negative");
            }
            Map.Entry<Float, List<Event>> entry = events.pollFirstEntry();
            float t = entry.getKey();
            float dt = t - universalTime;
            particles.forEach(p -> p.updatePosition(dt, L));
            for (Event event : entry.getValue()) {
                System.out.println(event.getClass());
                List<Particle> affectedParticles = event.execute(dt);
                for (Particle p : affectedParticles) {
                    float newt = p.timeToHitWall(L) + t;
                    events.putIfAbsent(newt, new ArrayList<>());
                    events.get(newt).add(new WallCollisionEvent(p, L));
                    updateAssociatedTimes(p, universalTime);

                }
            }

            FileController.writeParticlesState("output/state.txt", particles, true); // TODO: indicate which particles/walls are colliding
            universalTime = t;
//            System.out.println(universalTime);
        }
    }

}
