package Integrators;

public class GPC extends Integrator {

    private static final double alpha0 = 3.0 / 16.0;
    private static final double alpha1 = 251.0 / 360.0;
    private static final double alpha2 = 1.0;
    private static final double alpha3 = 11.0 / 18.0;
    private static final double alpha4 = 1.0 / 6.0;
    private static final double alpha5 = 1.0 / 60.0;
    private double r0, r1, r2, r3, r4, r5;

    public GPC(Particle1D p, double r0, double r1, double r2, double r3, double r4, double r5) {
        super(p);
        this.r0 = r0;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }

    @Override
    public void update(double dt, double k, double g) {
        double r0pNext = r0 + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6 + r4 * Math.pow(dt, 4) / 24 + r5 * Math.pow(dt, 5) / 120;
        double r1pNext = r1 + r2 * dt + r3 * Math.pow(dt, 2) / 2 + r4 * Math.pow(dt, 3) / 6 + r5 * Math.pow(dt, 4) / 24;
        double r2pNext = r2 + r3 * dt + r4 * Math.pow(dt, 2) / 2 + r5 * Math.pow(dt, 3) / 6;
        double r3pNext = r3 + r4 * dt + r5 * Math.pow(dt, 2) / 2;
        double r4pNext = r4 + r5 * dt;
        double r5pNext = r5;

        double r2Next = particle.getAcceleration(r0pNext, r1pNext, k, g);
        double DR2 = (r2Next - r2pNext) * dt * dt / 2;

        r0 = r0pNext + alpha0 * DR2;
        r1 = r1pNext + alpha1 * DR2 / dt;
        r2 = r2pNext + alpha2 * DR2 * 2 / (dt * dt);
        r3 = r3pNext + alpha3 * DR2 * 6 / (Math.pow(dt, 3));
        r4 = r4pNext + alpha4 * DR2 * 24 / (Math.pow(dt, 4));
        r5 = r5pNext + alpha5 * DR2 * 120 / (Math.pow(dt, 5));

        particle.setR(r0);
        particle.setV(r1);
    }
}
