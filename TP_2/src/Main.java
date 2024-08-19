import java.lang.annotation.Inherited;
import java.util.Objects;

public class Main {
    public static String BASE_PATH = "output/";
    public static void main(String[] args) {
        GOLAutomata();
        //seedsAutomata();
        //basic3DAutomata();
        lineAutomata();
        ringAutomata();
        ringAutomata3D();
        evenAutomata();
    }

    public static void GOLAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D("init2d010a")), AutomatonRules.GOL);
        final String filename = BASE_PATH + "gol2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println("stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void seedsAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D("init2d010a")), AutomatonRules.seeds);

        final String filename = BASE_PATH + "seeds2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println("stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void ringAutomata() {
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D("init2d010a")), AutomatonRules.ring);

        final String filename = BASE_PATH + "ring2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println("stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void lineAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D("init2d010a")), AutomatonRules.line);
        final String filename = BASE_PATH + "line2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println("stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void evenAutomata(){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D("init2d010a")), AutomatonRules.even2D);
        final String filename = BASE_PATH + "even2d.txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println("stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void ringAutomata3D() {
        CelularAutomata3D ca = new CelularAutomata3D(Objects.requireNonNull(FileController.loadFromFile3D("init3d010")), AutomatonRules.ring3D);
        final String filename = BASE_PATH + "ring3d.txt";
        FileController.createFile3D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile3D(filename, ca);
            if (ca.update()) break;
        }
        FileController.appendToFile3D(filename, ca);
    }


    public static void basic3DAutomata() {
        CelularAutomata3D ca = new CelularAutomata3D(Objects.requireNonNull(FileController.loadFromFile3D("init3d010")), (automata, i, j, k) -> {
            int sum = automata.sumNeighbors(i, j, k, 1, true);
            if (automata.getGridCell(i,j, k)) {
                sum = sum - 1;
                return sum >= 3 && sum <= 7;
            } else {
                return sum >= 4 && sum <= 9;
            }
        });
        final String filename = BASE_PATH + "gol3d.txt";
        FileController.createFile3D(filename, ca);
        FileController.appendToFile3D(filename, ca);
        for (int i = 0; i < 105; i++) {
            FileController.appendToFile3D(filename, ca);
            if (ca.update()) break;
        }
    }


}