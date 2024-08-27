import java.io.File;
import java.lang.annotation.Inherited;
import java.util.List;
import java.util.Objects;

public class Main {
    public static String INPUT_PATH = "input/";
    public static String BASE_PATH = "output/";
    public static void main(String[] args) {

        for (Integer initialState : List.of(10,20,30,40,50,60,70,80,90,99)) {
            AutomatonRules.Rules2D rule = AutomatonRules.Rules2D.GOLV2;
            runAutomata2D(initialState.toString(), rule.rule, rule.name);
        }

        for (Integer initialState : List.of(1,5,10,15,20,25,30,35,50,75)) {
            AutomatonRules.Rules2D rule = AutomatonRules.Rules2D.FILL;
            runAutomata2D(initialState.toString(), rule.rule, rule.name);
        }

        for (Integer initialState : List.of(1,10,20,30,40,50,60,70,80,90)) {
            AutomatonRules.Rules2D rule = AutomatonRules.Rules2D.ODD;
            runAutomata2D(initialState.toString(), rule.rule, rule.name);
        }


        for (Integer initialState : List.of(100, 500, 1000, 2000,3000,4000, 5000,6000,7200)) {
            AutomatonRules.Rules3D rule = AutomatonRules.Rules3D.GOL;
            runAutomata3D(initialState.toString(), rule.rule, rule.name);
        }

        for (Integer initialState : List.of(100, 1000, 2000,3000,4000, 5000,6000, 6500,7000,7500)) {
            AutomatonRules.Rules3D rule = AutomatonRules.Rules3D.DECAY;
            runAutomata3D(initialState.toString(), rule.rule, rule.name);
        }

        for (Integer initialState : List.of(100, 500, 1000, 2000, 3000, 4000, 5000,6000, 7000,7500)) {
            AutomatonRules.Rules3D rule = AutomatonRules.Rules3D.DECAYV2;
            runAutomata3D(initialState.toString(), rule.rule, rule.name);
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
        System.out.println("Finished " + name + " with initial state " + initName);
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
        System.out.println("Finished " + name + " with initial state " + initName);
    }
}