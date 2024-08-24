public class CelularAutomata3D implements  CelularAutomata{
    private boolean [][][] grid;
    private boolean [][][] nextGrid;

    private int length;
    private Rule3D rule;

    public CelularAutomata3D(int length, Rule3D rule) {
        this.grid = new boolean[length][length][length];
        this.nextGrid = new boolean[length][length][length];
        this.length = length;
        this.rule = rule;
    }

    public CelularAutomata3D(boolean [][][] grid, Rule3D rule) {
        this.grid = grid;
        this.nextGrid = new boolean[grid.length][grid[0].length][grid[0][0].length];
        this.length = grid.length;
        this.rule = rule;
    }

    public boolean inBounds(int i, int j, int k) {
        return i >= 0 && i < length && j >= 0 && j < length && k >= 0 && k < length;
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

    public int getLength() {
        return length;
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
        for (int i = 0; i < length; i++) {
            if(grid[i][0][0] || grid[i][length -1][0] || grid[i][0][length -1] || grid[i][length -1][length -1]){
                return true;
            }
        }
        for (int j = 0; j < length; j++) {
            if(grid[0][j][0] || grid[length -1][j][0] || grid[0][j][length -1] || grid[length -1][j][length -1]){
                return true;
            }
        }
        for (int k = 0; k < length; k++) {
            if(grid[0][0][k] || grid[0][length -1][k] || grid[length -1][0][k] || grid[length -1][length -1][k]){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < length; k++) {
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
