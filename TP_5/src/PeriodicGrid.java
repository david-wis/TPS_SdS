import java.awt.*;
import java.util.*;
import java.util.List;

public class PeriodicGrid {
    private final Set<Obstacle>[][] grid;
    private final int total; // Number of Particles
    private final int rows; // Number of cells of a row
    private final int cols; // Number of cells of a column
    private final double rc; // Interaction radius
    private final Map<Particle, List<Obstacle>> collisionMap;

    private static final Point[] directionsToCheck = new Point[] {
            new Point(0, 0),
            new Point(0, 1),
            new Point(1, 0),
            new Point(1, 1),
            new Point(-1,1)
    };

    public PeriodicGrid(int total) {
        Config config = Config.getConfig();
        double maxD = Math.max(2*config.getOBSTACLE_RADIUS(), 2* config.getR());
        this.total = total;
        this.rows = (int) Math.floor(config.getW() / maxD);
        this.cols = (int) Math.floor(config.getL() / maxD);
        this.rc = 0;

        this.grid = (Set<Obstacle>[][]) new Set[cols][rows];
        for (int i = 0; i < cols; i++)
            for (int j = 0; j < rows; j++)
                grid[i][j] = new HashSet<>();
        collisionMap = new HashMap<>();
    }

    private Point getObstacleCellPosition(final Obstacle o) {
        Config config = Config.getConfig();
        final int x = (int) (o.getX() / (config.getL() / cols));
        final int y = (int) (o.getY() / (config.getW() / rows));
        return new Point(x, y);
    }

    public void addEntity(final Obstacle o) {
        final Point cell = getObstacleCellPosition(o);
        if (cell.x > cols || cell.y > rows)
            System.out.println();
        grid[cell.x][cell.y].add(o);
    }

    public void updateCollisionMap(final List<Particle> particles, final List<Obstacle> obstacles) {
        particles.forEach(p -> collisionMap.put(p, new ArrayList<>()));
        List<Obstacle> entities = new ArrayList<>(obstacles);
        entities.addAll(particles);
        for (Obstacle e1 : entities) {
            final Point cell = getObstacleCellPosition(e1);
            for (Point d : directionsToCheck)
                checkNeighborsOfCell(cell, d, e1);
        }
    }

    private void checkNeighborsOfCell(final Point cell, final Point d, final Obstacle e1) {
        if (isInBounds(cell, d)) {
            for (Obstacle e2 : grid[modularSum(cell.x, d.x)][d.y+cell.y]) {
                if (insideInteractionRadius(e1, e2) && !e1.equals(e2)) {
                    if (e1 instanceof Particle)
                        collisionMap.get(e1).add(e2);
                    if (e2 instanceof Particle)
                        collisionMap.get(e2).add(e1);
                }
            }
        }
    }

    public Set<Obstacle>[][] getGrid() {
        return grid;
    }

    public int getTotal() {
        return total;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getRc() {
        return rc;
    }

    public Map<Particle, List<Obstacle>> getCollisionMap() {
        return collisionMap;
    }

    protected boolean insideInteractionRadius(Obstacle o1, Obstacle o2) {
        return o1.distance(o2) <= this.getRc();
    }

    protected boolean isInBounds(final Point p1, final Point p2) {
        double pos = p1.y + p2.y;
        return pos >= 0 && pos < this.getRows();
    }

    protected int modularSum(final int a, final int b) {
        return (a + b + this.getCols()) % this.getCols();
    }
}
