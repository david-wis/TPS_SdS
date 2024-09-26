package Integrators;

public class Verlet extends Integrator {
    private float rPrev;
    private float rCurr;
    public Verlet(Particle1D p, float r0, float r1) {
        super(p);
        rPrev = r0;
        rCurr = r1;
    }
    @Override
    public void update(float dt, float k, float gamma) {
        float vCurr = particle.getV();
        float rNext = 2 * rCurr - rPrev + dt * dt * particle.getAcceleration(rCurr, vCurr, k, gamma);
        float vNext = (rNext - rPrev) / (2 * dt);
        rPrev = rCurr;
        rCurr = rNext;
        particle.setR(rCurr);
        particle.setV(vNext);
    }

}
