import java.util.ArrayList;
import java.util.Collections;
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

            @Override
            public double capV(double v, double r) {
                return v;
            }
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

            @Override
            public double capV(double v, double y) {
//                if (y < 0 || y > Config.getConfig().getW())
//                    return 0;
                return v;
            }
        };
        Config config = Config.getConfig();

        this.integratorX = new Beeman(XProjection, config.getA0());
        this.integratorY = new Beeman(YProjection, 0);
    }

    private static int PARTICLE_DEBUG = 7;
    public Vector2D getA(boolean current) {
        Config config = Config.getConfig();
        List<Obstacle> entities = grid.getCollisionMap().getOrDefault(this, Collections.emptyList());
        Vector2D f = new Vector2D(config.getA0() * m, 0);
        Vector2D upperWallForce = getCollisionForce(new Obstacle(-1, this.getX(), Math.min(0, this.getY() - this.r), Math.abs(Math.min(0, this.getY() - this.r))), current);
        f = f.add(upperWallForce);
        if (config.isDEBUG() && upperWallForce.magnitude() > 1E-6 && this.id == PARTICLE_DEBUG)
            System.out.println("    upperWallForce: f " + upperWallForce.toString() + " magnitude: " + upperWallForce.magnitude() + " pos " + this.getPos());
        Vector2D lowerWallForce = getCollisionForce(new Obstacle(-1, this.getX(), Math.max(config.getW(), this.getY() + this.r), Math.abs(Math.min(0, config.getW() - this.getY() - this.r))), current);
        if (config.isDEBUG() && lowerWallForce.magnitude() > 1E-6 && this.id == PARTICLE_DEBUG)
            System.out.println("    lowerWallForce: f " + lowerWallForce.toString() + " magnitude: " + lowerWallForce.magnitude() + " pos " + this.getPos());
        f = f.add(lowerWallForce);

        if (!entities.isEmpty() && config.isDEBUG() && this.id == PARTICLE_DEBUG)
            System.out.println(" obstacles: " + entities.size());
        for (Obstacle e : entities) {
            Vector2D force = getCollisionForce(e, current);
            if (force.magnitude() > 1E-6 && config.isDEBUG() && this.id == PARTICLE_DEBUG) {
                // obstacle position
                System.out.println("    o:" + e.id + " (" + e.getClass() + ") ");
                System.out.println("        position: " + e.getPos() + " overlap: " + (this.r + e.getR() - e.distance(this)) + " r: " + e.getR());
                System.out.println("        force: f " + force.toString() + " magnitude: " + force.magnitude());
            }
            f = f.add(force);
        }
        if (config.isDEBUG() && this.id == PARTICLE_DEBUG)
            System.out.println("Particle: " + this.id + ", Position: " + this.pos  + ", Resultant force  : " + f.toString());
        return f.scale(1.0/m);
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
            Vector2D relV = current ? this.getV().subtract(e.getV()) : this.getPredictedV().subtract(e.getPredictedV());

            if (this.id == PARTICLE_DEBUG && config.isDEBUG()) {
                System.out.println("Relative velocity: " + relV);
                System.out.println("My velocity: " + (current? this.getV() : this.getPredictedV()));
                System.out.println("Other velocity: " + (current? e.getV() : e.getPredictedV()));
            }

            Vector2D tangential = normal.normal(); // Dirección tangencial

            double FN_magnitude = -kN * overlap - gamma * relV.magnitude();
            Vector2D FN = normal.scale(FN_magnitude);

            double FT_magnitude = -kT * overlap * relV.dot(tangential); // T.3
            Vector2D FT = tangential.scale(FT_magnitude);

            return FN.add(FT);
        } else {
            return new Vector2D(0, 0); // No hay contacto
        }   
    }




    public double getM() {
        return m;
    }

    private double mod(double x, double L) {
        return ((x % L) + L) % L;
    }
    public void setX(double x) {
        Config config = Config.getConfig();
        if (Double.isNaN(x)) {
            System.err.println("AAAAA");
//            throw new IllegalArgumentException("Particle " + this.id + " has x = NaN");
        }
        pos.setX(mod(x, config.getL()));
    }

    public void setY(double y) {
        // cap the y position to the grid (if y < 0, then 0, if y > W, then W)
//        double EPSILON = 1E-6;
//        Config config = Config.getConfig();
//        if (y < 0) {
//            y = 0;
//            setVy(Math.max(0, getV().getY()));
////            setVy(0);
//        } else if (y > config.getW())  {
//            y = config.getW() - EPSILON;
//            setVy(Math.min(0, getV().getY()));
////            setVy(0);
//        }
        pos.setY(y);
    }


    public void setVx(double vx) {
        v.setX(vx);
//        if (v.magnitude() > 20) {
//            System.out.println();
//        }
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
        return this.getId() + ", pos: " + this.getPos() + ", v: " + this.getV().toString();
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

        public abstract double capV(double v, double r);
    }
}

