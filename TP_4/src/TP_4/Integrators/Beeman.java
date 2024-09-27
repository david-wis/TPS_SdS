package TP_4.Integrators;

import TP_4.Particle;

public class Beeman extends Integrator{
    double aPrev;

    public Beeman(Particle p, double a0) {
        super(p);
        this.aPrev = a0;
    }

    @Override
    public void update(double t, double dt) {
        double rCurr = particle.getR();
        double vCurr = particle.getV();
        double aCurr = particle.getAcceleration(rCurr, vCurr, t);
        double rNext = rCurr + vCurr * dt + ((2.0/3) * aCurr - (1.0/6) * aPrev) * dt * dt;
        double vpNext = vCurr + (3.0/2) * aCurr * dt - (1.0/2) * aPrev * dt;

        double acNext = particle.getAcceleration(rNext, vpNext, t + dt);
        double vcNext = vCurr + (1.0/3) * acNext * dt + (5.0/6) * aCurr * dt - (1.0/6) * aPrev * dt;

        particle.setR(rNext);
        particle.setV(vcNext);
        aPrev = aCurr;
    }
}
