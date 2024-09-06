import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileController {

    public static void writeParticlesState(String filename, List<Particle> particles, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Particle p : particles)
                writer.write(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getR() + " " + p.getVx() + " " + p.getVy() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static List<Particle> readParticlesState(String filename) {
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            List<Particle> particles = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] data = line.split(" ");
                int id = Integer.parseInt(data[0]);
                float x = Float.parseFloat(data[1]);
                float y = Float.parseFloat(data[2]);
                float r = Float.parseFloat(data[3]);
                float vx = Float.parseFloat(data[4]);
                float vy = Float.parseFloat(data[5]);
                Particle p = new Particle(id, x, y, vx, vy, r, 1);
                particles.add(p);
            }
            return particles;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return Collections.emptyList();
    }


}
