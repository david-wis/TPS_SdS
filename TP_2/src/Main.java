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
        runAutomata2D("025a", AutomatonRules.fill, "fill2d");
        runAutomata2D("025b", AutomatonRules.fill, "fill2d");
        runAutomata2D("010a", AutomatonRules.fill, "fill2d");
        runAutomata2D("025a", AutomatonRules.expansion2D, "expansion2d");
        runAutomata2D("001", AutomatonRules.expansion2D, "expansion2d");

        runAutomata3D("010", AutomatonRules.ring3D, "ring3d");
        runAutomata3D("010", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("025", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("125", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("1000", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("8000", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("025", AutomatonRules.expansion3d, "expansion3d");
    }

    public static void runAutomata2D(String initName, CelularAutomata2D.Rule2D rule, String name){
        CelularAutomata2D ca = new CelularAutomata2D(Objects.requireNonNull(FileController.loadFromFile2D(INPUT_PATH + "init2d" + initName + ".txt")), rule);
        String path = BASE_PATH + name + initName + "2d";
        String filename = path + "/" + name  + initName + "2d.txt";
        FileController.createFile2D(path, filename, ca);
        FileController.appendToFile2D(filename, ca);
        for (int i = 0; i < 100; i++) {
            if (ca.update()) {
                System.out.println(name + " stopped at iteration " + i);
                break;
            }
            FileController.appendToFile2D(filename, ca);
        }
    }

    public static void runAutomata3D(String initName, CelularAutomata3D.Rule3D rule, String name){
        CelularAutomata3D ca = new CelularAutomata3D(Objects.requireNonNull(FileController.loadFromFile3D(INPUT_PATH + "init3d" + initName + ".txt")), rule);
        String path = BASE_PATH + name + initName + "3d";
        String filename = path + "/" + name  + initName + "3d.txt";
        FileController.createFile3D(path, filename, ca);
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