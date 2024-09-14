import java.util.List;

public class WallCollisionEvent implements Event {
    private final Particle p;
    private final float L;

    private final Wall[] ws;

    public enum Wall {
        LEFT, RIGHT, TOP, BOTTOM
    };

    public WallCollisionEvent(Particle p, float L, Wall[] ws) {
        this.p = p;
        this.L = L;
        this.ws = ws;
    }

    @Override
    public List<Particle> execute(float dt) {
        p.bounceOffWall(L, ws);
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
