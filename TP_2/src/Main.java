public class Main {
    public static void main(String[] args) {
        GOLAutomata();
        //seedsAutomata();
        //basic3DAutomata();
        lineAutomata();
        ringAutomata();
        ringAutomata3D();
    }

    public static void GOLAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(100, 100, AutomatonRules.GOL);
        // glider
        int x=50;
        int y=50;
        ca.setBit(x+0, y+0, true);

        ca.setBit(x+1, y+1, true);
        ca.setBit(x+1, y+2, true);
        ca.setBit(x+2, y+0, true);
        ca.setBit(x+2, y+1, true);
        final String filename = "gol2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) break;
        }
    }

    public static void seedsAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(100, 100, AutomatonRules.seeds);
        // glider
        int x=50;
        int y=50;
        ca.setBit(x+0, y+0, true);

        ca.setBit(x+1, y+1, true);
        ca.setBit(x+1, y+2, true);
        ca.setBit(x+2, y+0, true);
        ca.setBit(x+2, y+1, true);

        final String filename = "seeds2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) break;
        }
    }

    public static void ringAutomata() {
        CelularAutomata2D ca = new CelularAutomata2D(19, 19, AutomatonRules.ring);
        // glider
        int x=ca.getRows()/2;
        int y=ca.getCols()/2;
        ca.setBit(x+0, y+0, true);
        ca.setBit(x+2, y+2, true);
        ca.setBit(x-2, y+2, true);
        ca.setBit(x+2, y-2, true);
        ca.setBit(x-2, y-2, true);
//        ca.setBit(x-1, y+0, true);

//        ca.setBit(x+1, y+1, true);
//        ca.setBit(x+1, y+2, true);
//        ca.setBit(x+2, y+0, true);
//        ca.setBit(x+2, y+1, true);

        final String filename = "ring2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) break;
        }
        FileController.appendToFile2D(filename, ca);

    }

    public static void lineAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(100, 100, AutomatonRules.line);
        // glider
        int x=50;
        int y=50;
        ca.setBit(x+0, y+0, true);

        ca.setBit(x+1, y+1, true);
        ca.setBit(x+1, y+2, true);
        ca.setBit(x+2, y+0, true);
        ca.setBit(x+2, y+1, true);

        final String filename = "line2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) break;
        }
    }


    public static void ringAutomata3D() {
        CelularAutomata3D ca = new CelularAutomata3D(19, 19, 19, AutomatonRules.ring3D);
        // glider
        int x=ca.getRows()/2;
        int y=ca.getCols()/2;
        int z=ca.getDepth()/2;
        ca.setBit(x+0, y+0, z+0, true);
//        ca.setBit(x-1, y+0, true);

//        ca.setBit(x+1, y+1, true);
//        ca.setBit(x+1, y+2, true);
//        ca.setBit(x+2, y+0, true);
//        ca.setBit(x+2, y+1, true);

        final String filename = "ring3d.txt";
        FileController.createFile3D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile3D(filename, ca);
            if (ca.update()) break;
        }
        FileController.appendToFile3D(filename, ca);

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