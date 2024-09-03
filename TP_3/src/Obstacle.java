public class Obstacle {
    private final float x, y, r;

    public Obstacle(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public float distance(final Particle p) {
        return (float) Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2)) - this.r - p.getR();
    }
}
