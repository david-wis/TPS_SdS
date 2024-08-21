import java.lang.annotation.Inherited;
import java.util.Objects;

public class Main {
    public static String INPUT_PATH = "input/";
    public static String BASE_PATH = "output/";
    public static void main(String[] args) {
        runAutomata2D("010a", AutomatonRules.GOL, "gol2d");
        runAutomata2D("010a", AutomatonRules.seeds, "seeds2d");
        runAutomata2D("010a", AutomatonRules.ring, "ring2d");
        runAutomata2D("010a", AutomatonRules.line, "line2d");
        runAutomata2D("010a", AutomatonRules.even2D, "even2d");
        runAutomata2D("025a", AutomatonRules.odd2D, "odd2d");

        runAutomata3D("010", AutomatonRules.ring3D, "ring3d");

    }

    public static void runAutomata2D(String initName, CelularAutomata2D.Rule2D rule, String name){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D(INPUT_PATH + "init2d" + initName + ".txt")), rule);
        String filename = BASE_PATH + name  + initName + ".txt";
        FileController.createFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile2D(filename, ca);
            if (ca.update()) {
                System.out.println(name + " stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile2D(filename, ca);
    }

    public static void runAutomata3D(String initName, CelularAutomata3D.Rule3D rule, String name){
        CelularAutomata3D ca = new CelularAutomata3D(Objects.requireNonNull(FileController.loadFromFile3D(INPUT_PATH + "init3d" + initName + ".txt")), rule);
        String filename = BASE_PATH + name + initName + ".txt";
        FileController.createFile3D(filename, ca);
        for (int i = 0; i < 100; i++) {
            FileController.appendToFile3D(filename, ca);
            if (ca.update()) {
                System.out.println(name + " stopped at iteration " + i);
                break;
            }
        }
        FileController.appendToFile3D(filename, ca);
    }
}