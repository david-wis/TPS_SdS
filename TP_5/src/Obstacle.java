public class Obstacle {
    protected int id;
    protected double x, y, r;

    public Obstacle(int id, double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double distance(final Obstacle o) {
        Config config = Config.getConfig();
        double L = config.getL();

        double dx = o.getX() - this.getX();

        dx = dx - L * Math.round(dx / L); // TODO: Revisar

        double dy = o.getY() - this.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        dist = Math.max(0, dist - (this.getR() + o.getR()));

        return dist;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public int getId() {
        return id;
    }
}
