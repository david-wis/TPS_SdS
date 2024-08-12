import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileController {

    public static void writeParticlesState(String filename, List<Particle> particles) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (Particle p : particles)
                writer.write(p.getId() + " " + p.getX() + " " + p.getY() + "\n");
//                writer.write(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeCIMOutput(String filename, boolean[][] adjacencyMatrix, int N, Grid grid) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(grid.getL() + "\n");
            writer.write(grid.getM() + "\n");
            writer.write(grid.getN() + "\n");
            writer.write(grid.getRc() + "\n");
            for (int i = 0; i < N; i++) {
                writer.write(i + " : ");
                for (int j = 0; j < N; j++)
                    if (adjacencyMatrix[i][j])
                        writer.write(j + " ");
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the output.");
        }
    }
}
