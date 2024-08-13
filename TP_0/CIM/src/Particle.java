import java.util.Objects;

public class Particle {
    private int id;
    private float x;
    private float y;
    private float r;

    public Particle(int id, float x, float y, float r) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public float distance(final Particle p) {
        return (float) Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2)) - this.r - p.r;
    }

    public float modularDistance(final Particle p, final float L) {
        return (float) Math.sqrt(Math.pow(Math.min(Math.abs(this.x - p.x), L - Math.abs(this.x - p.x)), 2) + Math.pow(Math.min(Math.abs(this.y - p.y), L - Math.abs(this.y - p.y)), 2)) - this.r - p.r;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }



}
