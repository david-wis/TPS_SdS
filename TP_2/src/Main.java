public class Main {
    public static void main(String[] args) {
        CelularAutomata2D ca = new CelularAutomata2D(100, 100, (automata, i, j) -> {
            int sum = automata.sumNeighbors(i, j, 1, true);
            if (automata.getGridCell(i,j)) {
                sum = sum - 1;
                return sum == 2 || sum == 3;
            } else {
                return sum == 3;
            }
        });
        // glider
        ca.setBit(0, 0, true);
        ca.setBit(1, 1, true);
        ca.setBit(1, 2, true);
        ca.setBit(2, 0, true);
        ca.setBit(2, 1, true);

        FileController.createFile2D("output.txt", ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D("output.txt", ca);
            ca.update();
        }
    }
}