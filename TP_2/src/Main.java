public class Main {
    public static void main(String[] args) {
//        GOLAutomata();
        basic3DAutomata();
    }

    public static void GOLAutomata(){
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
        final String filename = "gol2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) break;
        }
    }

    public static void basic3DAutomata() {
        CelularAutomata3D ca = new CelularAutomata3D(10, 10, 10, (automata, i, j, k) -> {
            int sum = automata.sumNeighbors(i, j, k, 1, true);
            if (automata.getGridCell(i,j, k)) {
                sum = sum - 1;
                return sum >= 3 && sum <= 7;
            } else {
                return sum >= 4 && sum <= 9;
            }
        });
        // glider
        int x0 = 4;
        int y0 = 4;
        int z0 = 4;
        ca.setBit(x0 + 0, y0 + 0,  z0 + 0,  true);
        ca.setBit(x0 + 0, y0 + 1,  z0 + 1, true);
        ca.setBit(x0 + 0, y0 + 1,  z0 + 2, true);
        ca.setBit(x0 + 0, y0 + 2,  z0 + 0, true);
        ca.setBit(x0 + 0, y0 + 2,  z0 + 1, true);
        final String filename = "gol3d.txt";
        FileController.createFile3D(filename, ca);
        FileController.appendToFile3D(filename, ca);
        for (int i = 0; i < 105; i++) {
            FileController.appendToFile3D(filename, ca);
            if (ca.update()) break;
        }
    }


}