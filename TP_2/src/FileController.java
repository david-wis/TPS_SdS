import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FileController {
    public static void init() {
        File file = new File("output");
        if (!file.exists() && file.mkdir())
            System.out.println("Output directory is created!");

    }

    //TODO optimize writer for opening and closing once ?
    public static void createFile2D(String filename, CelularAutomata2D ca) {
        init();
        try {
            FileWriter writer = new FileWriter(filename, false);
            writer.write(ca.getRows() + "\n");
            writer.write(ca.getCols() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }
    public static void appendToFile2D(String filename, CelularAutomata2D ca) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (int i = 0; i < ca.getRows(); i++) {
                for (int j = 0; j < ca.getCols(); j++)
                    writer.write((ca.getGridCell(i, j) ? 1 : 0) + " ");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }

    public static boolean[][] loadFromFile2D(String filename) {
        try {
            File file = new File(
                    filename);
            Scanner sc = new Scanner(file);
            int rows = Integer.parseInt(sc.nextLine());
            int cols = Integer.parseInt(sc.nextLine());
            boolean[][] grid = new boolean[rows][cols];
            String line = sc.nextLine();
            int[] a = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < a.length; i++) {
                grid[i / cols][i % cols] = a[i] == 1;
            }

            return grid;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return null;
    }

    public static void createFile3D(String filename, CelularAutomata3D ca) {
        init();
        try {
            FileWriter writer = new FileWriter(filename, false);
            writer.write(ca.getRows() + "\n");
            writer.write(ca.getCols() + "\n");
            writer.write(ca.getDepth() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }

    public static void appendToFile3D(String filename, CelularAutomata3D ca) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (int i = 0; i < ca.getRows(); i++) {
                for (int j = 0; j < ca.getCols(); j++) {
                    for (int k = 0; k < ca.getDepth(); k++)
                        writer.write((ca.getGridCell(i, j, k) ? 1 : 0) + " ");
                }
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }

    public static boolean[][][] loadFromFile3D(String filename) {
        try {
            File file = new File(
                    filename);
            Scanner sc = new Scanner(file);
            int rows = Integer.parseInt(sc.nextLine());
            int cols = Integer.parseInt(sc.nextLine());
            int depth = Integer.parseInt(sc.nextLine());
            boolean[][][] grid = new boolean[rows][cols][depth];
            String line = sc.nextLine();
            int[] a = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < a.length; i++) {
                grid[i / (cols * depth)][(i / depth) % cols][i % depth] = a[i] == 1;
            }

            return grid;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return null;
    }

}
