package TP_4;

public class Particle {

    protected double r;
    protected double v;
    protected double m;

    protected Particle() {
    }

    public Particle(double r, double v) {
        this.r = r;
        this.v = v;
        this.m = Config.getConfig1().getM();
    }

    public Particle(double t) {
        this(0, 0);
        this.r = calculatePosition(t);
        this.v = calculateVelocity(t);
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
    public Particle copy(){
        return new Particle(r, v);
    }

    public double getAcceleration(double r, double v, double t) {
        Config config = Config.getConfig1();
        double k = config.getK();
        double g = config.getG();
        return (-k * r - g * v) / m;
    }

    public double calculatePosition(double t) {
        Config config = Config.getConfig1();
        double k = config.getK();
        double g = config.getG();
        double r0 = config.getR0();
        return (r0 * Math.exp(-g * t / (2 * this.m) ) * Math.cos(Math.sqrt(k / this.m - Math.pow(g / (2 * this.m), 2)) * t));
    }

    public double calculateVelocity(double t) {
        Config config = Config.getConfig1();
        double k = config.getK();
        double g = config.getG();
        double r0 = config.getR0();
        return (-(g / (2 * m)) * r0 * Math.exp(-g * t / (2 * m)) * Math.cos(Math.sqrt(k / m - Math.pow(g / (2 * m), 2)) * t) - r0 * Math.exp(-g * t / (2 * m)) * Math.sin(Math.sqrt(k / m - Math.pow(g / (2 * m), 2)) * t) * Math.sqrt(k / m - Math.pow(g / (2 * m), 2)));
    }
}
