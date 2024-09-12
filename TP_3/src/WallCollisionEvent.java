import java.util.List;

public class WallCollisionEvent implements Event {
    private final Particle p;
    private final float L;
    public WallCollisionEvent(Particle p, float L) {
        this.p = p;
        this.L = L;
    }

    @Override
    public List<Particle> execute(float dt) {
        p.bounceOffWall(L);
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
}
