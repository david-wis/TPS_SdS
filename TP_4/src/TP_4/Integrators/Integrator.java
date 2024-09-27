package TP_4.Integrators;

import TP_4.Particle;

public abstract class Integrator {
    protected Particle particle;


    public Integrator(Particle particle) {
        this.particle = particle;
    }

    public abstract void update(double t, double dt);

    public Particle getParticle() {
        return particle;
    }
}
