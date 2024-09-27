package TP_4;

public class LinkedParticle extends Particle {
    private final int position;
    private final LinkedParticle[] particles;

    public LinkedParticle(int position, LinkedParticle[] particles) {
        super();
        this.position = position;
        this.particles = particles;
        this.r = 0;
        this.v = 0;
        this.m = Config.getConfig2().getM();
    }

    public LinkedParticle(double r, double v, int position) {
        super();
        this.r = r;
        this.v = v;
        this.m = Config.getConfig2().getM();
        this.position = position;
        this.particles = null;
    }

    public LinkedParticle[] getParticles() {
        return particles;
    }


    @Override
    public double getAcceleration(double r, double v, double t) {
        Config config = Config.getConfig2();
        double k = config.getK();
        double A = config.getA();
        double w = config.getW();
        double N = config.getN();
        if (position == 0) return 0;
        if (position == N) {
            double armForce = A * Math.cos(w * t);
            return armForce -k * (r - particles[position - 1].r) / m;
        }
        return -k * (2*r - particles[position - 1].r - particles[position + 1].r) / m;
    }


    @Override
    public Particle copy() {
        return new LinkedParticle(this.r , this.v, this.position);
    }
}
