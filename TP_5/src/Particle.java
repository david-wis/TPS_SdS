import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle extends Obstacle{
    private double m;
    private ParticleProjection XProjection, YProjection;
    private Beeman integratorX, integratorY;
    private PeriodicGrid grid;
    public Particle(int id, double x, double y, double r, double m) {
        super(id, x, y, r);
        this.m = m;
        XProjection = new ParticleProjection() {
            @Override
            public double getR() { return x; }

            @Override
            public double getV() { return v.getX(); }

            @Override
            public double getA(boolean current) { return Particle.this.getA(current).getX(); }

            @Override
            public void setR(double r) { setX(r); }

            @Override
            public void setV(double v) { setVx(v); }
        };

        YProjection = new ParticleProjection() {
            @Override
            public double getR() { return y; }

            @Override
            public double getV() { return v.getY(); }

            @Override
            public double getA(boolean current) { return Particle.this.getA(current).getY(); }

            @Override
            public void setR(double r) { setY(r); }

            @Override
            public void setV(double v) { setVy(v); }
        };
        Config config = Config.getConfig();

        this.integratorX = new Beeman(XProjection, config.getA0());
        this.integratorY = new Beeman(YProjection, 0);
    }

    public Vector2D getA(boolean current) {
        Config config = Config.getConfig();
        List<Obstacle> entities = grid.getCollisionMap().get(this);
        Vector2D a = new Vector2D(config.getA0(), 0);
        a = a.add(getCollisionForce(new Obstacle(-1, this.getX(), Math.min(0, this.getY() - this.r), Math.abs(Math.min(0, this.getY() - this.r))), current).scale(1.0/m));
        a = a.add(getCollisionForce(new Obstacle(-1, this.getX(), Math.max(config.getW(), this.getY() + this.r), Math.abs(Math.min(0, config.getW() - this.getY() - this.r))), current).scale(1.0/m));

        for (Obstacle e : entities) {
            a = a.add(getCollisionForce(e, current).scale(1.0/m));
        }
        return a;
    }

    public Vector2D getCollisionForce(Obstacle e, boolean current) {
        Config config = Config.getConfig();
        Vector2D dr = e.getPos().subtract(this.pos);
        double dt = config.getDT();
        double kN = config.getKN();
        double kT = kN * 2;
        double distance = dr.magnitude();
        double overlap = this.r + e.getR() - distance;

        if (overlap > 0) {
            Vector2D normal = dr.unitVector(); // Dirección normal
            Vector2D relV = current? e.getV().subtract(this.v) : e.getPredictedV().subtract(this.getPredictedV());

            // Calcular la fuerza normal (usando la opción N.2)
            double FN_magnitude = -config.getKN() * overlap; // TODO : Gamma?
            Vector2D FN = normal.scale(FN_magnitude);

            // Calcular la fuerza tangencial (usando la opción T.3)
            Vector2D tangential = normal.normal(); // Dirección tangencial
            double FT_magnitude = -kT * overlap * relV.dot(tangential); // T.3
            Vector2D FT = tangential.scale(FT_magnitude);

            // Fuerza total
            return FN.add(FT);
        } else {
            return new Vector2D(0, 0);
        }
    }



    public double getM() {
        return m;
    }

    public void setX(double x) {
        Config config = Config.getConfig();
        if (x >= config.getL())
            x -= config.getL();
        else if(x < 0)
            x += config.getL();
        pos.setX(x);
    }

    public void setY(double y) {
//        if (y < r || y + r >= Config.getConfig().getW())
//            throw new IllegalArgumentException("Particle " + this.getId() + " is out of bounds");
        pos.setY(y);
    }

    public void setVx(double vx) {
        v.setX(vx);
    }

    public void setVy(double vy) {
        v.setY(vy);
    }

    public PeriodicGrid getGrid() {
        return grid;
    }

    @Override
    public Vector2D getPredictedV() {
        return new Vector2D(integratorX.vpNext, integratorY.vpNext);
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
        return this.getId() + ", " + this.getX() + ", " + this.getY();
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


    public abstract class ParticleProjection{
        public abstract double getR();
        public abstract double getV();
        public abstract double getA(boolean current);
        public abstract void setR(double r);
        public abstract void setV(double r);

    }
}
