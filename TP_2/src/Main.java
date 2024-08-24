import java.io.File;
import java.lang.annotation.Inherited;
import java.util.List;
import java.util.Objects;

public class Main {
    public static String INPUT_PATH = "input/";
    public static String BASE_PATH = "output/";
    public static void main(String[] args) {
        runAutomata2D("10", AutomatonRules.GOL, "gol2d");
//        runAutomata2D("10", AutomatonRules.seeds, "seeds2d");
//        runAutomata2D("10", AutomatonRules.ring, "ring2d");
//        runAutomata2D("10", AutomatonRules.line, "line2d");
//        runAutomata2D("10", AutomatonRules.even2D, "even2d");
//        runAutomata2D("25", AutomatonRules.odd2D, "odd2d");
//        runAutomata2D("25", AutomatonRules.fill, "fill2d");
//        runAutomata2D("25", AutomatonRules.fill, "fill2d");
//        runAutomata2D("10", AutomatonRules.fill, "fill2d");
//        runAutomata2D("25", AutomatonRules.expansion2D, "expansion2d");
//        runAutomata2D("1", AutomatonRules.expansion2D, "expansion2d");

//        runAutomata3D("10", AutomatonRules.ring3D, "ring3d");
//        runAutomata3D("010", AutomatonRules.decay3D, "decay3d");
//        runAutomata3D("025", AutomatonRules.decay3D, "decay3d");
//        runAutomata3D("125", AutomatonRules.decay3D, "decay3d");
//        runAutomata3D("1000", AutomatonRules.decay3D, "decay3d");
        runAutomata3D("7200", AutomatonRules.decay3D, "decay3d");
//        runAutomata3D("025", AutomatonRules.expansion3d, "expansion3d");
    }

    public static void runAutomata2D(String initName, CelularAutomata2D.Rule2D rule, String name){
        List<boolean[][]> initialStates = FileController.loadFromFile2D(INPUT_PATH + "init2d_" + initName + ".txt");
        String path = BASE_PATH + name + initName + "_2d";
        String filename = path + "/" + name + initName + "_2d.txt";
        FileController.createFile2D(path, filename, initialStates.get(0).length, initialStates.get(0)[0].length);
        for (boolean[][] initialState : initialStates) {
            FileController.startNewRun(path, filename);
            CelularAutomata2D ca = new CelularAutomata2D(initialState, rule);
            FileController.appendToFile2D(filename, ca);
            for (int i = 0; i < 100; i++) {
                if (ca.update()) {
                    System.out.println(name + " stopped at iteration " + i);
                    break;
                }
                FileController.appendToFile2D(filename, ca);
            }
        }
    }

    public static void runAutomata3D(String initName, CelularAutomata3D.Rule3D rule, String name){
        List<boolean[][][]> initialStates = FileController.loadFromFile3D(INPUT_PATH + "init3d_" + initName + ".txt");
        String path = BASE_PATH + name + initName + "_3d";
        String filename = path + "/" + name  + initName + "_3d.txt";
        FileController.createFile3D(path, filename, initialStates.get(0).length, initialStates.get(0)[0].length, initialStates.get(0)[0][0].length);
        for (boolean[][][] initialState : initialStates) {
            FileController.startNewRun(path, filename);
            CelularAutomata3D ca = new CelularAutomata3D(initialState, rule);
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
}