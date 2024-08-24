import java.io.File;
import java.lang.annotation.Inherited;
import java.util.List;
import java.util.Objects;

public class Main {
    public static String INPUT_PATH = "input/";
    public static String BASE_PATH = "output/";
    public static void main(String[] args) {
        List<Integer> initialStates2D = List.of(1,10,25,50,75,99);
        for (AutomatonRules.Rules2D rule : AutomatonRules.Rules2D.values()) {
            for (Integer initialState : initialStates2D) {
                runAutomata2D(initialState.toString(), rule.rule, rule.name);
                System.out.println("Finished " + rule.name + " with initial state " + initialState);
            }
        }

        List<Integer> initialStates3D = List.of(10, 100, 500, 1000, 4000, 7200);
        for (AutomatonRules.Rules3D rule : AutomatonRules.Rules3D.values()) {
            for (Integer initialState : initialStates3D) {
                runAutomata3D(initialState.toString(), rule.rule, rule.name);
                System.out.println("Finished " + rule.name + " with initial state " + initialState);
            }
        }
    }

    public static void runAutomata2D(String initName, CelularAutomata2D.Rule2D rule, String name){
        List<boolean[][]> initialStates = FileController.loadFromFile2D(INPUT_PATH + "init2d_" + initName + ".txt");
        String path = BASE_PATH + name;
        String filename = path + "/" + initName + ".txt";
        FileController.createFile(path, filename, initialStates.get(0).length, 10);
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
        String path = BASE_PATH + name;
        String filename = path + "/" + initName + ".txt";
        FileController.createFile(path, filename, initialStates.get(0).length, 20);
        for (boolean[][][] initialState : initialStates) {
            FileController.startNewRun(path, filename);
            CelularAutomata3D ca = new CelularAutomata3D(initialState, rule);
            FileController.appendToFile3D(filename, ca);
            for (int i = 0; i < 100; i++) {
                if (ca.update()) {
                    System.out.println(name + " stopped at iteration " + i);
                    break;
                }
                FileController.appendToFile3D(filename, ca);
            }
        }
    }
}