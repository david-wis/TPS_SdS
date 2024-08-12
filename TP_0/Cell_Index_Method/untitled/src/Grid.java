import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grid {
    private final Set<Particle>[][] grid;
    private final int N; // Number of particles
    private final float L; // Length
    private final int M; // Number of cells of a row
    private final float rc; // Interaction radius

    public Grid(int n, float l, int m, float rc) {
        this.N = n;
        this.L = l;
        this.M = m;
        this.rc = rc;

        this.grid = (Set<Particle>[][]) new Set[M][M];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
                grid[i][j] = new HashSet<>();

    }

    private Point getParticleCellPosition(final Particle p) {
        final int x = (int) (p.getX() / (L / M));
        final int y = (int) (p.getY() / (L / M));
        return new Point(x, y);
    }

    public void addParticle(final Particle p) {
        final Point cell = getParticleCellPosition(p);
        grid[cell.x][cell.y].add(p);
    }

    private final static Point[] directionsToCheck = new Point[] {
            new Point(0, 0),
            new Point(0, 1),
            new Point(1, 0),
            new Point(1, 1),
            new Point(-1,1)
    };

    protected boolean isInBounds(final Point p1, final Point p2) {
        return p1.x + p2.x < M && p1.y + p2.y < M && p1.x + p2.x >= 0 && p1.y + p2.y >= 0;
    }

    public boolean[][] getParticlesInRadiusForAll(final List<Particle> particles) {
        boolean[][] adjacencyMatrix = new boolean[N][N];

        for (Particle p1 : particles) {
            final Point cell = getParticleCellPosition(p1);
            for (Point d : directionsToCheck)
                checkNeighborsOfCell(cell, d, p1, adjacencyMatrix);
        }
        return adjacencyMatrix;
    }

    protected int modularSum(final int a, final int b) {
        return a + b;
    }

    private void checkNeighborsOfCell(final Point cell, final Point d, final Particle p1, final boolean[][] adjacencyMatrix) {
        if (isInBounds(cell, d)) {
            for (Particle p2 : grid[modularSum(cell.x, d.x)][modularSum(cell.y, d.y)]) {
                if (p1.distance(p2) <= rc && !p1.equals(p2)) {
                    adjacencyMatrix[p1.getId()][p2.getId()] = true;
                    adjacencyMatrix[p2.getId()][p1.getId()] = true;
                }
            }
        }
    }

    public Set<Particle>[][] getGrid() {
        return grid;
    }

    public int getN() {
        return N;
    }

    public float getL() {
        return L;
    }

    public int getM() {
        return M;
    }

    public float getRc() {
        return rc;
    }
}
