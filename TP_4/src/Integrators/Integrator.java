package Integrators;

public abstract class Integrator {
    protected Particle1D particle;


    public Integrator(Particle1D particle) {
        this.particle = particle;
    }

    public abstract void update(double dt, double k, double g);

    public Particle1D getParticle() {
        return particle;
    }
}
