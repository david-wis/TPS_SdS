import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileController {


    public static void writeObstacleState(String filename, List<Obstacle> obstacles, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Obstacle o : obstacles)
                writer.write(o.getId() + " " + o.getX() + " " + o.getY() + " " + o.getR() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeParticlesState(String filename, List<Particle> particles, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Particle p : particles)
                writer.write(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getR() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeAnalysis(String filename, List<Particle> particles, double t, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            if (!particles.isEmpty())
                writer.write(t + ":" + particles.stream().map(Particle::getId).toList() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the analysis.");
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
                double x = Double.parseDouble(data[1]);
                double y = Double.parseDouble(data[2]);
                double r = Double.parseDouble(data[3]);
                Particle p = new Particle(id, x, y, r, 1);
                particles.add(p);
            }
            return particles;
        } catch (IOException e) {
            System.err.println("An error occurred reading the input.");
        }
        return Collections.emptyList();
    }

    public static List<Obstacle> readObstaclesState(String filename) {
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            List<Obstacle> obstacles = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] data = line.split(" ");
                int id = Integer.parseInt(data[0]);
                double x = Double.parseDouble(data[1]);
                double y = Double.parseDouble(data[2]);
                double r = Double.parseDouble(data[3]);
                Obstacle o = new Obstacle(id, x, y, r);
                obstacles.add(o);
            }
            return obstacles;
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
