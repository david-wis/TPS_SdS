import java.util.List;

public class ParticleCollisionEvent implements Event {
    private final Particle p1, p2;

    public ParticleCollisionEvent(Particle p1, Particle p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public List<Particle> execute(float dt) {

        float dx = p2.getX() - p1.getX();
        float dy = p2.getY() - p1.getY();

        float dvx = p2.getVx() - p1.getVx();
        float dvy = p2.getVy() - p1.getVy();

        float dvdr = dx*dvx + dy*dvy;

//        float dist = p1.getR() + p2.getR();
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        float J = 2 * p1.getM() * p2.getM() * dvdr / ((p1.getM() + p2.getM()) * dist);
        float Jx = J * dx / dist;
        float Jy = J * dy / dist;

        p1.bounceOffParticle(Jx, Jy);
        p2.bounceOffParticle(-Jx, -Jy);

        return List.of(p1, p2);
    }

    @Override
    public boolean particleInEvent(Particle p) {
        return p.equals(p1) || p.equals(p2);
    }

    @Override
    public List<Particle> getParticles(){ return List.of(p1,p2);}
}
