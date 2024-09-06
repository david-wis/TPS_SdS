import java.util.Objects;

public class Particle {
    private final  int id;
    private float x, y, vx, vy;
    private final float r, m;

    public Particle(int id, float x, float y, float vx, float vy, float r, float m) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.m = m;
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

    public float timeToHitWall(float L) {
        float dtX = (this.vx > 0) ? (L - this.r - this.x) / this.vx : (0 + this.r - this.x) / this.vx;
        float dtY = (this.vy > 0) ? (L - this.r - this.y) / this.vy : (0 + this.r - this.y) / this.vy;
        if (dtX < 0 || dtY < 0){
            throw new IllegalStateException("Time cannot be negative");
        }
        return Math.min(dtX, dtY);
    }

    public void updatePosition(float dt, float L) {
        this.x = Math.min(L - this.r, Math.max(this.r, this.x + this.vx * dt));
        this.y = Math.min(L - this.r, Math.max(this.r, this.y + this.vy * dt));
    }

    public void bounceOffWall(float L) {
        // get epsilon
        float EPSILON = 1e-6f;
        if (this.x + this.r >= L - EPSILON || this.x - this.r <= 0 + EPSILON) {
            this.vx = -this.vx;
        }
        if (this.y + this.r >= L - EPSILON|| this.y - this.r <= 0 + EPSILON) {
            this.vy = -this.vy;
        }
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getR() {
        return r;
    }

    public float getM() {
        return m;
    }
}
