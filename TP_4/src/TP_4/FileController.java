package TP_4;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class FileController {

    public static void writeParticlesState(String filename, List<Particle> particles, double t, boolean append) {
        try {
            FileWriter writer = new FileWriter(filename, append);
            for (Particle p : particles)
                writer.write(t + " " + p.getR() + " " + p.getV() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred writing the initial state.");
        }
    }

    public static void writeParticlesState(String filename, Particle particles, double t, boolean append) {
        writeParticlesState(filename, Collections.singletonList(particles), t, append);
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
