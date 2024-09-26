package Integrators;

public class Particle1D {

    private double r;
    private double v;
    private double m;

    public Particle1D(double r, double v, double m) {
        this.r = r;
        this.v = v;
        this.m = m;
    }

    public Particle1D(double m, double t, double r0, double k, double g) {
        this(0, 0, m);
        this.r = calculatePosition(r0, k, g, t);
        this.v = calculateVelocity(r0, k, g, t);
    }

    public double getR() {
        return r;
    }

    public double getV() {
        return v;
    }

    public double getM() {
        return m;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setM(double m) {
        this.m = m;
    }
    public Particle1D copy(){
        return new Particle1D(r, v, m);
    }

    public double getAcceleration(double r, double v, double k, double g) {
        return (-k * r - g * v) / m;
    }

    public double calculatePosition(double r0, double k, double g, double t) {
        return (double) (r0 * Math.exp(-g * t / (2 * this.m) ) * Math.cos(Math.sqrt(k / this.m - Math.pow(g / (2 * this.m), 2)) * t));
    }

    public double calculateVelocity(double r0, double k, double g, double t) {
        return (double) (-(g / (2 * m)) * r0 * Math.exp(-g * t / (2 * m)) * Math.cos(Math.sqrt(k / m - Math.pow(g / (2 * m), 2)) * t) - r0 * Math.exp(-g * t / (2 * m)) * Math.sin(Math.sqrt(k / m - Math.pow(g / (2 * m), 2)) * t) * Math.sqrt(k / m - Math.pow(g / (2 * m), 2)));
    }
}
