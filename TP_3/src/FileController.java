import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class FileController {

    public static void writeParticlesState(String filename, List<Particle> particles, boolean append) {
        writeParticlesState(filename, particles, Collections.emptySet(), append);
    }

    public static void writeParticlesState(String filename, List<Particle> particles, Set<Particle> affected, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Particle p : particles)
                writer.write(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getR() + " " + p.getVx() + " " + p.getVy() + " " + (affected.contains(p) ? "1" : "0") + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeObstaclePositions(String filename, TreeMap<Float, Particle> obstacles, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Map.Entry<Float, Particle> p : obstacles.entrySet())
                writer.write(p.getKey() + " " + p.getValue().getX() + " " + p.getValue().getY() + "\n");
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

    public static void createEmptyFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred creating the file.");
        }
    }

    public static void writeEvent(String filename, TreeMap<Float, List<Event>> events) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (Map.Entry<Float, List<Event>> entry : events.entrySet()) {
                for (Event e : entry.getValue()) {
                    writer.write(entry.getKey() + " " + e.toString() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the events.");
        }
    }

    public static Config getConfig(String filePath) {

        // Create a Gson instance
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON file to Person object
            Config config = gson.fromJson(reader, Config.class);
            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Config();
    }


    public static void createFolderIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists() && file.mkdir())
            System.out.println(path + " directory is created!");
    }
}
