public class CelularAutomata3D {
    private boolean [][][] grid;
    private boolean [][][] nextGrid;

    private int rows;
    private int cols;
    private int depth;
    private Rule3D rule;

    public CelularAutomata3D(int rows, int cols, int depth, Rule3D rule) {
        this.grid = new boolean[rows][cols][depth];
        this.nextGrid = new boolean[rows][cols][depth];
        this.rows = rows;
        this.cols = cols;
        this.depth = depth;
        this.rule = rule;
    }

    public CelularAutomata3D(boolean [][][] grid, Rule3D rule) {
        this.grid = grid;
        this.nextGrid = new boolean[grid.length][grid[0].length][grid[0][0].length];
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.depth = grid[0][0].length;
        this.rule = rule;
    }

    public boolean[][][] update() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < depth; k++) {
                    nextGrid[i][j][k] = rule.apply(this, i, j, k);
                }
            }
        }
        boolean [][][] temp = nextGrid;
        //swap new grid to grid, and reuse the old grid for the next iteration
        nextGrid = grid;
        grid = temp;
        return grid; //return the new grid
    }

    @FunctionalInterface
    public interface Rule3D {
        boolean apply(CelularAutomata3D automata, int i, int j, int k);
    }
}
