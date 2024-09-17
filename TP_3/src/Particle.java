import java.util.ArrayList;
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
//        float EPSILON = 1e-6f;

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
            return Float.POSITIVE_INFINITY;
//            System.out.println(p.id + " - " + this.id);
//            System.out.println(this + ", " + p + " Distance: " + this.distance(p.get(P_2)));
//            throw new IllegalStateException("Time to hit particle cannot be negative");
        }
        return dt;
    }

    public record WallHit(float dt, WallCollisionEvent.Wall[] ws) {}

    public WallHit timeToHitWall(float L) {
        float dtX = 0, dtY = 0;
        WallCollisionEvent.Wall[] ws = new WallCollisionEvent.Wall[2];

        if (this.vx >= 0) {
            dtX = (L - this.r - this.x) / this.vx;
            ws[0] = WallCollisionEvent.Wall.RIGHT;
        } else if (this.vx < 0) {
            dtX = (0 + this.r - this.x) / this.vx;
            ws[0] = WallCollisionEvent.Wall.LEFT;
        }

        if (this.vy >= 0) {
            dtY = (L - this.r - this.y) / this.vy;
            ws[1] = WallCollisionEvent.Wall.TOP;
        } else if (this.vy < 0) {
            dtY = (0 + this.r - this.y) / this.vy;
            ws[1] = WallCollisionEvent.Wall.BOTTOM;
        }

        if (dtX < 0 || dtY < 0) throw new IllegalStateException("Time to hit wall cannot be negative");

        float EPSILON = 1e-6f;
        float dt = dtX;
        if (Math.abs(dtX - dtY) < EPSILON){
        }
        else
        if (dtX < dtY)
            ws[1] = null;
        else if (dtY < dtX) {
            ws[0] = null;
            dt = dtY;
        }

        return new WallHit(dt, ws);
    }

    public float timeToHitObstacle(Obstacle o) {
        float EPSILON = 1e-6f;

        float dx = x - o.getX();
        float dy = y - o.getY();
        float vdx = vx;
        float vdy = vy;

        float sigma = r + o.getR();

        float A = vdx * vdx + vdy * vdy;
        float B = 2 * (dx * vdx + dy * vdy);
        float C = dx * dx + dy * dy - sigma*sigma;

        float disc = B * B - 4 * A * C;

        if (disc < 0)
            return Float.POSITIVE_INFINITY;

        float sqrtDiscr = (float) Math.sqrt(disc);
        float t1 = (-B - sqrtDiscr) / (2 * A);
        float t2 = (-B + sqrtDiscr) / (2 * A);

        float t = Math.min(t1, t2);
        if (t < EPSILON){
//            System.out.println("=================Time to hit obstacle is negative, id: " + this.id);
            return Float.POSITIVE_INFINITY;
        }
        return t;
    }

    public void updatePosition(float dt, float L) {
        this.x = Math.min(L - this.r, Math.max(this.r, this.x + this.vx * dt));
        this.y = Math.min(L - this.r, Math.max(this.r, this.y + this.vy * dt));
    }

    public void bounceOffWall(float L, WallCollisionEvent.Wall[] ws) {
        if (ws[0] == WallCollisionEvent.Wall.LEFT || ws[0] == WallCollisionEvent.Wall.RIGHT)
            this.vx = -this.vx;
        if (ws[1] == WallCollisionEvent.Wall.TOP || ws[1] == WallCollisionEvent.Wall.BOTTOM)
            this.vy = -this.vy;
    }

    public void bounceOffParticle(float Jx, float Jy) {
        this.vx += Jx / m;
        this.vy += Jy / m;
    }

    public void bounceOffObstacle(float nx, float ny) {
        float dvdr = vx*nx + vy*ny;
        this.vx -= 2 * dvdr * nx;
        this.vy -= 2 * dvdr * ny;
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
    public String toString() {
        return this.getX() + ", " + this.getY() + ", " + this.getVx() + ", " + this.getVy();
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
