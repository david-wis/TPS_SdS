package Integrators;

public class Verlet extends Integrator {
    private double rPrev;
    public Verlet(Particle1D p, double r0) {
        super(p);
        rPrev = r0;
    }
    @Override
    public void update(double dt, double k, double g) {
        double rCurr = particle.getR();
        double vCurr = particle.getV();
        double rNext = 2 * rCurr - rPrev + dt * dt * particle.getAcceleration(rCurr, vCurr, k, g);
        double vNext = (rNext - rPrev) / (2 * dt);
        particle.setR(rNext);
        particle.setV(vNext);
        rPrev = rCurr;
    }


}
