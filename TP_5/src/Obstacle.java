public class Obstacle {
    protected int id;
    protected double r;
    protected Vector2D pos;
    protected Vector2D v;

    public Obstacle(int id, double x, double y, double r) {
        this.id = id;
        this.pos = new Vector2D(x, y);
        this.r = r;
        this.v = new Vector2D(0, 0);
    }

    public double distance(final Obstacle o) {
        Config config = Config.getConfig();
        double L = config.getL();

        double dx = o.getX() - this.getX();

        dx = dx - L * Math.round(dx / L); // TODO: Revisar

        double dy = o.getY() - this.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
//        dist = dist - (this.getR() + o.getR());

        return dist;
    }


    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public double getR() {
        return r;
    }

    public Vector2D getPos() {
        return pos;
    }

    public int getId() {
        return id;
    }

    public Vector2D getV() {
        return v;
    }

    public Vector2D getPredictedV() {
        return v;
    }

}
