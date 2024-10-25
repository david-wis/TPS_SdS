import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle extends Obstacle{
    private double vx, vy;
    private double m;
    private ParticleProjection XProjection, YProjection;
    private Beeman integratorX, integratorY;
    private PeriodicGrid grid;
    public Particle(int id, double x, double y, double r, double m) {
        super(id, x, y, r);
        this.vx = 0;
        this.vy = 0;
        this.m = m;
        XProjection = new ParticleProjection() {
            @Override
            public double getR() { return x; }

            @Override
            public double getV() { return vx; }

            @Override
            public double getA() { return ax; }

            @Override
            public void setR(double r) { setX(r); }

            @Override
            public void setV(double v) { setVx(v); }
        };

        YProjection = new ParticleProjection() {
            @Override
            public double getR() { return y; }

            @Override
            public double getV() { return vy; }

            @Override
            public double getA() { return ay; }

            @Override
            public void setR(double r) { setY(r); }

            @Override
            public void setV(double v) { setVy(v); }
        };
        Config config = Config.getConfig();

        this.integratorX = new Beeman(XProjection, config.getA0());
        this.integratorY = new Beeman(YProjection, 0);
    }


    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getR() {
        return r;
    }

    public double getM() {
        return m;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getAxCurr() {
        Config config = Config.getConfig();
        List<Obstacle> entities = grid.getCollisionMap().get(this);
        double collisionA = 0;
        for (Obstacle e : entities) {
            if (e instanceof Particle) {
//                Particle p = (Particle) e;
//                double distance = distance(p);
//                double overlap = r + p.getR() - distance;
//                double normalX = (p.getX() - x) / distance;
//                double normalY = (p.getY() - y) / distance;
//                double relativeVx = vx - p.getVx();
//                double relativeVy = vy - p.getVy();
//                double relativeVn = relativeVx * normalX + relativeVy * normalY;
//                double J = (1 + config.getE()) * relativeVn / (1 / m + 1 / p.getM());
//                collisionA += J / m;

            }
        }
        return config.getA0();
    }

    public double getAxNext() {
        return 0;
    }
    public double getAyCurr() {
        return 0;
    }
    public double getAyNext() {
        return 0;
    }

    public PeriodicGrid getGrid() {
        return grid;
    }

    public void setGrid(PeriodicGrid grid) {
        this.grid = grid;
    }

    public Beeman getIntegratorX() {
        return integratorX;
    }


    public Beeman getIntegratorY() {
        return integratorY;
    }


    @Override
    public String toString() {
        return this.getId() + ", " + this.getX() + ", " + this.getY() + ", " + this.getVx() + ", " + this.getVy();
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


    @Override
    public Particle clone() {
        return new Particle(id, x, y, r, m);
    }

    public abstract class ParticleProjection{
        public abstract double getR();
        public abstract double getV();
        public abstract double getA();
        public abstract void setR(double r);
        public abstract void setV(double r);

    }
}
