import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileController {
    public static void createFolderIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists() && file.mkdir())
            System.out.println(path + " directory is created!");
    }

    public static void startNewRun(String path, String filename) {
        try{
            FileWriter writer = new FileWriter(filename, true);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }
    //TODO optimize writer for opening and closing once ?
    public static void createFile(String path, String filename, int length, int core) {
        createFolderIfNotExists("output");
        createFolderIfNotExists(path);
        try{
            FileWriter writer = new FileWriter(filename, false);
            writer.write(length + "\n");
            writer.write(core + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }


    public static void appendToFile2D(String filename, CelularAutomata2D ca) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (int i = 0; i < ca.getLength(); i++) {
                for (int j = 0; j < ca.getLength(); j++)
                    writer.write((ca.getGridCell(i, j) ? '1' : '0'));
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }

    public static List<boolean[][]> loadFromFile2D(String filename) {
        try {
            List<boolean[][]> states = new ArrayList<>();
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            int length = Integer.parseInt(sc.nextLine());
            int core = Integer.parseInt(sc.nextLine());
            while (sc.hasNextLine()) {
                boolean[][] grid = new boolean[length][length];
                String line = sc.nextLine();
                int[] a = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray();
                for (int i = 0; i < a.length; i++) {
                    grid[i / length][i % length] = a[i] == 1;
                }
                states.add(grid);
            }
            return states;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return Collections.emptyList();
    }

    public static void appendToFile3D(String filename, CelularAutomata3D ca) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (int i = 0; i < ca.getLength(); i++) {
                for (int j = 0; j < ca.getLength(); j++) {
                    for (int k = 0; k < ca.getLength(); k++)
                        writer.write((ca.getGridCell(i, j, k) ? "1" : "0"));
                }
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }

    public static List<boolean[][][]> loadFromFile3D(String filename) {
        try {
            List<boolean[][][]> states = new ArrayList<>();
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            int length = Integer.parseInt(sc.nextLine());
            int core = Integer.parseInt(sc.nextLine());
            while (sc.hasNextLine()) {
                boolean[][][] grid = new boolean[length][length][length];
                String line = sc.nextLine();
                int[] a = Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray();
                for (int i = 0; i < a.length; i++) {
                    grid[i / (length * length)][(i / length) % length][i % length] = a[i] == 1;
                }
                states.add(grid);
            }
            return states;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return Collections.emptyList();
    }
}
