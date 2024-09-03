import java.util.*;

public class Field {
    private final float L;
    private List<Particle> particles;
    private Obstacle obstacle;
    public Field(float l, List<Particle> particles, Obstacle obstacle) {
        L = l;
        this.particles = particles;
        this.obstacle = obstacle;
    }

    public void loop() {
        TreeMap<Float, List<Event>> events = new TreeMap<>();
        for (Particle p : particles) {
            float t = p.timeToHitWall(L);
            events.putIfAbsent(t, new ArrayList<>());
            events.get(t).add(new WallCollisionEvent(p, L));
//            events.add(new ObstacleCollisionEvent(p, obstacle));
        }
        while (!events.isEmpty()) {
            Map.Entry<Float, List<Event>> entry = events.pollFirstEntry();
            float t = entry.getKey();
            for (Event event : entry.getValue()) {
                List<Particle> affectedParticles = event.execute(t);
                for (Particle p : affectedParticles) {
                    float newt = p.timeToHitWall(L);
                    events.putIfAbsent(t, new ArrayList<>());
                    events.get(newt).add(new WallCollisionEvent(p, L));
                }
            }
        }
    }

}
