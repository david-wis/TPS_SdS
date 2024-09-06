import java.util.*;

public class Field {
    private final float L;
    private List<Particle> particles;
    private Obstacle obstacle;
    private TreeMap<Float, List<Event>> events;

    public Field(float l, List<Particle> particles, Obstacle obstacle) {
        L = l;
        this.particles = particles;
        this.obstacle = obstacle;
    }

    private void init() {
        events = new TreeMap<>();
        for (Particle p : particles) {
            float t = p.timeToHitWall(L);
            events.putIfAbsent(t, new ArrayList<>());
            events.get(t).add(new WallCollisionEvent(p, L));
//            events.add(new ObstacleCollisionEvent(p, obstacle));
        }
    }

    public void loop(float duration) {
        init();
        FileController.writeParticlesState("output/state.txt", particles, false);

        float universalTime = 0;
        while (universalTime < duration) {
            if (universalTime < 0) {
                throw new IllegalStateException("Time cannot be negative");
            }
            Map.Entry<Float, List<Event>> entry = events.pollFirstEntry();
            float t = entry.getKey();
            float dt = t - universalTime;
            particles.forEach(p -> p.updatePosition(dt, L));
            for (Event event : entry.getValue()) {
                List<Particle> affectedParticles = event.execute(dt);
                for (Particle p : affectedParticles) {
                    float newt = p.timeToHitWall(L) + t;
//                    System.out.println(p.timeToHitWall(L));
//                    System.out.println(p.getX() + " " + p.getY());
//                    System.out.println(p.getVx() + " " + p.getVy());
                    events.putIfAbsent(newt, new ArrayList<>());
                    events.get(newt).add(new WallCollisionEvent(p, L));

                    // if the particle is colliding with two walls
                    if (p.getX() + p.getR() >= L && p.getY() + p.getR() >= L) {
                       System.out.println("Particle " + p.getId() + " is colliding with two walls");
                    }
                }
            }

            FileController.writeParticlesState("output/state.txt", particles, true); // TODO: indicate which particles/walls are colliding
            universalTime = t;
            System.out.println(universalTime);
        }
    }

}
