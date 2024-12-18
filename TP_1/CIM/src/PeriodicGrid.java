import java.awt.*;

public class PeriodicGrid extends Grid {

    public PeriodicGrid(int n, float l, int m, float rc) {
        super(n, l, m, rc);
    }

    @Override
    protected boolean insideInteractionRadius(Particle p1, Particle p2) {
        return p1.modularDistance(p2, this.getL()) <= this.getRc();
    }

    @Override
    protected boolean isInBounds(final Point p1, final Point p2) {
        return true;
    }

    @Override
    protected int modularSum(final int a, final int b) {
        return (a + b + this.getM()) % this.getM();
    }

}
