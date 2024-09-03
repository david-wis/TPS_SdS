import java.util.List;

public class WallCollisionEvent implements Event{
    private final Particle p;
    private final float L;
    public WallCollisionEvent(Particle p, float L) {
        this.p = p;
        this.L = L;
    }

    @Override
    public List<Particle> execute(float dt) {
        p.updatePosition(dt);
        p.bounceOffWall(L);
        return List.of(p);
    }

}
