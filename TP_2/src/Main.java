public class Main {
    public static void main(String[] args) {
        CelularAutomata2D ca = new CelularAutomata2D(100, 100, (automata, i, j) -> {
            int sum = automata.sumNeighbors(i, j, 1, true);
            if (automata.getGridCell(i,j)) {
                return sum == 2 || sum == 3;
            } else {
                return sum == 3;
            }
        });
        // glider
        ca.setBit(25, 25, true);
        ca.setBit(26, 26, true);
        ca.setBit(26, 27, true);
        ca.setBit(25, 27, true);
        ca.setBit(24, 27, true);

        FileController.createFile2D("output.txt", ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D("output.txt", ca);
            ca.update();
        }
    }
}