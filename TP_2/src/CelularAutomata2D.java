import java.util.function.Function;


public class CelularAutomata2D implements CelularAutomata {

    private boolean [][] grid;
    private boolean [][] nextGrid;
    private int rows;
    private int cols;
    private Rule2D rule;

    public CelularAutomata2D(int rows, int cols, Rule2D rule) {
        this.grid = new boolean[rows][cols];
        this.nextGrid = new boolean[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.rule = rule;
    }

    public CelularAutomata2D(boolean [][] grid, Rule2D rule) {
        this.grid = grid;
        this.nextGrid = new boolean[grid.length][grid[0].length];
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.rule = rule;
    }

    public void setBit(int i, int j, boolean value) {
        grid[i][j] = value;
    }

    public boolean inBounds(int i, int j) {
        return i >= 0 && i < rows && j >= 0 && j < cols;
    }

    public int sumNeighbors(int x, int y, int d, boolean moore) {
        int sum = 0;
        for (int i = -d; i <= d; i++) {
            for (int j = -d + (moore? 0 : Math.abs(i)); j <= d - (moore? 0 : Math.abs(i)); j++) {
                if (inBounds(x + i, y + j)) {
                    sum += grid[x + i][y + j] ? 1 : 0;
                }
            }
        }
        return sum;
    }
    @Override
    public void update() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nextGrid[i][j] = rule.apply(this, i, j);
            }
        }
        boolean [][] temp = nextGrid;
        //swap new grid to grid, and reuse the old grid for the next iteration
        nextGrid = grid;
        grid = temp;
    }

    public boolean getGridCell(int i, int j) {
        return grid[i][j];
    }

    public boolean[][] getGrid() {
        return grid.clone();
    }

    @Override
    public boolean borderReached(){
        for (int i = 0; i < rows; i++) {
            if(grid[i][0] || grid[i][cols-1]){
                return true;
            }
        }
        for (int j = 0; j < cols; j++) {
            if(grid[0][j] || grid[rows-1][j]){
                return true;
            }
        }
        return false;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @FunctionalInterface
    public interface Rule2D {

        boolean apply(CelularAutomata2D automata, int i, int j);
    }
}
