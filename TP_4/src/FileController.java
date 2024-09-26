import Integrators.Particle1D;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class FileController {

    public static void writeParticlesState(String filename, List<Particle1D> particles, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Particle1D p : particles)
                writer.write(p.getR() + " " + p.getV() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeParticlesState(String filename, Particle1D particles, boolean append) {
        writeParticlesState(filename, Collections.singletonList(particles), append);
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
