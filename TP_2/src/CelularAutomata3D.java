public class CelularAutomata3D implements  CelularAutomata{
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

    public boolean inBounds(int i, int j, int k) {
        return i >= 0 && i < rows && j >= 0 && j < cols && k >= 0 && k < depth;
    }

    public int sumNeighbors(int x, int y, int z,  int d, boolean moore) {
        int sum = 0;
        for (int i = -d; i <= d; i++) {
            for (int j = -d + (moore? 0 : Math.abs(i)); j <= d - (moore? 0 : Math.abs(i)); j++) {
                for (int k = -d + (moore? 0 : Math.abs(i) + Math.abs(j)); k <= d - (moore? 0 : Math.abs(i) + Math.abs(j)); k++) {
                    if (inBounds(x + i, y + j, z + k) && i != x && j != y && k != z) {
                        sum += grid[x + i][y + j][z + k] ? 1 : 0;
                    }
                }
            }
        }
        return sum;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getDepth() {
        return depth;
    }

    public boolean getGridCell(int i, int j, int k) {
        return grid[i][j][k];
    }

    public void setBit(int i, int j, int k, boolean value) {
        grid[i][j][k] = value;
    }

    public boolean[][][] getGrid() {
        return grid.clone();
    }

    @Override
    public boolean borderReached(){
        for (int i = 0; i < rows; i++) {
            if(grid[i][0][0] || grid[i][cols-1][0] || grid[i][0][depth-1] || grid[i][cols-1][depth-1]){
                return true;
            }
        }
        for (int j = 0; j < cols; j++) {
            if(grid[0][j][0] || grid[rows-1][j][0] || grid[0][j][depth-1] || grid[rows-1][j][depth-1]){
                return true;
            }
        }
        for (int k = 0; k < depth; k++) {
            if(grid[0][0][k] || grid[0][cols-1][k] || grid[rows-1][0][k] || grid[rows-1][cols-1][k]){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update() {
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
        return borderReached(); //|| Utils.compare3D(grid, nextGrid);
    }

    @FunctionalInterface
    public interface Rule3D {
        boolean apply(CelularAutomata3D automata, int i, int j, int k);
    }
}
