import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileController {
    //TODO optimize writer for opening and closing once ?
    public static void createFile2D(String filename, CelularAutomata2D ca) {
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
}
