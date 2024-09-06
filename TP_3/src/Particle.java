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


    public float distance(final Particle p) {
//        System.out.println((float) Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2)) - this.r - p.r);
        return (float) Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2)) - this.r - p.r;
    }

    public float timeToHitParticle(Particle p) {
        if (this.equals(p)) {
            throw new IllegalStateException("Cannot collide with itself");
        }

        float dx = p.x - this.x;
        float dy = p.y - this.y;

        float dvx = p.vx - this.vx;
        float dvy = p.vy - this.vy;

        float dvdr = dx * dvx + dy * dvy;
        if (dvdr > 0) {
            return Float.POSITIVE_INFINITY;
        }

        float dvdv = dvx * dvx + dvy * dvy;
        float drdr = dx * dx + dy * dy;

        float sigma = this.r + p.r;
        float d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);

        if (d < 0) {
            return Float.POSITIVE_INFINITY;
        }
        float dt = -(dvdr + (float) Math.sqrt(d)) / dvdv;
//        System.out.println(dvdr + " " + Math.sqrt(d) + " " + dvdv + " " + dt);
        if (dt < 0) {
            throw new IllegalStateException("Time to hit particle cannot be negative");
        }
        return dt;
    }

    public float timeToHitWall(float L) {
        float dtX = (this.vx > 0) ? (L - this.r - this.x) / this.vx : (0 + this.r - this.x) / this.vx;
        float dtY = (this.vy > 0) ? (L - this.r - this.y) / this.vy : (0 + this.r - this.y) / this.vy;
        if (dtX < 0 || dtY < 0) throw new IllegalStateException("Time to hit wall cannot be negative");
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

    public void bounceOffParticle(float Jx, float Jy) {
        this.vx += Jx / m;
        this.vy += Jy / m;
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
}
