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
        float tX = (this.vx > 0) ? (L - this.r - this.x) / this.vx : (0 + this.r - this.x) / this.vx;
        float tY = (this.vy > 0) ? (L - this.r - this.y) / this.vy : (0 + this.r - this.y) / this.vy;
        return Math.min(tX, tY);
    }

    public void updatePosition(float dt) {
        this.x += this.vx * dt;
        this.y += this.vy * dt;
    }

    public void bounceOffWall(float L) {
        if (this.x + this.r >= L || this.x - this.r <= 0) {
            this.vx = -this.vx;
        }
        if (this.y + this.r >= L || this.y - this.r <= 0) {
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
