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
            public double getR() { return Particle.this.getX(); }

            @Override
            public double getV() { return Particle.this.getV().getX(); }

            @Override
            public double getA(boolean current) { return Particle.this.getA(current).getX(); }

            @Override
            public void setR(double r) { setX(r); }

            @Override
            public void setV(double v) { setVx(v); }
        };

        YProjection = new ParticleProjection() {
            @Override
            public double getR() { return Particle.this.getY(); }

            @Override
            public double getV() { return Particle.this.getV().getY(); }

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
        Vector2D upperWallForce = getCollisionForce(new Obstacle(-1, this.getX(), Math.min(0, this.getY() - this.r), Math.abs(Math.min(0, this.getY() - this.r))), current).scale(1.0/m);
        a = a.add(upperWallForce);
        if (upperWallForce.magnitude() > 1E-6)
            System.out.println("    upperWallForce: f " + upperWallForce.toString() + " magnitude: " + upperWallForce.magnitude() + " pos " + this.getPos());
        Vector2D lowerWallForce = getCollisionForce(new Obstacle(-1, this.getX(), Math.max(config.getW(), this.getY() + this.r), Math.abs(Math.min(0, config.getW() - this.getY() - this.r))), current).scale(1.0/m);
        if (lowerWallForce.magnitude() > 1E-6)
            System.out.println("    lowerWallForce: f " + lowerWallForce.toString() + " magnitude: " + lowerWallForce.magnitude() + " pos " + this.getPos());
        a = a.add(lowerWallForce);

        for (Obstacle e : entities) {
            a = a.add(getCollisionForce(e, current).scale(1.0/m));
        }
        if (!entities.isEmpty())
            System.out.println("    a: " + a.toString() + " entities: " + entities.size() + " id: " + this.getId() + " x: " + this.getX() + " y: " + this.getY() + " r: " + this.getR() + " v: " + this.getV().magnitude());
        return a;
    }

    public Vector2D getCollisionForce(Obstacle e, boolean current) {
        Config config = Config.getConfig();
        Vector2D dr = e.getPos().subtract(this.pos);
        double dt = config.getDT();
        double kN = config.getKN();
        double kT = kN * 2; // Constante elástica tangencial
        double gamma = config.getG(); // Coeficiente de amortiguamiento
        double distance = dr.magnitude();
        double overlap = this.r + e.getR() - distance;

        if (overlap > 0) {
            Vector2D normal = dr.unitVector(); // Dirección normal
            Vector2D relV = current ? e.getV().subtract(this.v) : e.getPredictedV().subtract(this.getPredictedV());

            // Descomposición de la velocidad relativa en componentes normal y tangencial
            double relV_normal = relV.dot(normal); // Proyección en la dirección normal
            Vector2D tangential = normal.normal(); // Dirección tangencial

            // Calcular la fuerza normal (usando la opción N.1)
            double FN_magnitude = -kN * overlap - gamma * relV_normal;
            Vector2D FN = normal.scale(FN_magnitude);

            // Calcular la fuerza tangencial (usando la opción T.3)
            double FT_magnitude = -kT * overlap * relV.dot(tangential); // T.3
            Vector2D FT = tangential.scale(FT_magnitude);

            // Fuerza total
            return FN.add(FT);
        } else {
            return new Vector2D(0, 0); // No hay contacto
        }   
    }




    public double getM() {
        return m;
    }

    public void setX(double x) {
        Config config = Config.getConfig();
        pos.setX(x - config.getL() * Math.floor(x / config.getL()));
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
        return this.getId() + ", " + this.getPos() + ", " + this.getV().toString();
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
