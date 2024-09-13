import java.util.List;

public class ObstacleCollisionEvent implements Event {

    private final Particle p;
    private final Obstacle o;
    public ObstacleCollisionEvent(Particle p, Obstacle o) {
        this.p = p;
        this.o = o;
    }
    @Override
    public List<Particle> execute(float dt) {
        float dx = p.getX() - o.getX();
        float dy = p.getY() - o.getY();
        float dist = (float) Math.sqrt(dx*dx + dy*dy);

        float nx = dx/dist;
        float ny = dy/dist;


        p.bounceOffObstacle(nx, ny);

        return List.of(p);
    }

    @Override
    public boolean particleInEvent(Particle p) {
        return p.equals(this.p);
    }

    @Override
    public List<Particle> getParticles(){
        return List.of(p);
    }

    @Override
    public String toString() {
        return p.toString().replace(",", "");
    }
}
