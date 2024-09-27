package TP_4.Integrators;

import TP_4.Particle;

public class Verlet extends Integrator {
    private double rPrev;
    public Verlet(Particle p, double r0) {
        super(p);
        rPrev = r0;
    }
    @Override
    public void update(double t, double dt) {
        double rCurr = particle.getR();
        double vCurr = particle.getV();
        double rNext = 2 * rCurr - rPrev + dt * dt * particle.getAcceleration(rCurr, vCurr, t); // TODO: Check if this is correct
        double vNext = (rNext - rPrev) / (2 * dt);
        particle.setR(rNext);
        particle.setV(vNext);
        rPrev = rCurr;
    }


}
