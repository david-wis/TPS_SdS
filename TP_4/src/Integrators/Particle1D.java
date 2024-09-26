package Integrators;

public class Particle1D {

    private float r;
    private float v;
    private float mass;

    public Particle1D(float r, float v, float mass) {
        this.r = r;
        this.v = v;
        this.mass = mass;
    }

    public float getR() {
        return r;
    }

    public float getV() {
        return v;
    }

    public float getMass() {
        return mass;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setV(float v) {
        this.v = v;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
    public Particle1D copy(){
        return new Particle1D(r, v, mass);
    }

    public float getAcceleration(float r, float v, float k, float gamma) {
        return -k * r / mass - gamma * v / mass;
    }
}
