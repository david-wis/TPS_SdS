public class Vector2D {
    private double x,y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2D unitVector() {
        double m = magnitude();
        return new Vector2D(x/m, y/m);
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.getX(), y + v.getY());
    }

    public Vector2D subtract(Vector2D v) {
        return new Vector2D(x - v.getX(), y - v.getY());
    }

    public Vector2D scale(double s) {
        return new Vector2D(x*s, y*s);
    }

    public double dot(Vector2D v) {
        return x*v.getX() + y*v.getY();
    }

    public Vector2D normal() {
        return new Vector2D(-y, x);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }


}
