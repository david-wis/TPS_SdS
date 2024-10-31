import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static String BASE_PATH(int M, double A0, int seed){
        return String.format("output/%d/%.2g/%x", M, A0, seed);
    }


    public static void main(String[] args) {
        Config config = Config.getConfig();
        for (int m : config.getMS()) {
            config.setM(m);
            System.out.printf("\nM: %d\n", m);
            FileController.createFolderIfNotExists("output/" + m);
            for (double a0 : config.getA0S()) {
                config.setA0(a0);
                System.out.printf("\tA0: %g\n", a0);
                FileController.createFolderIfNotExists(String.format("output/%d/%.2g", m, a0));
                for (String seed : config.getSEEDS()) {
                    int seedHex = Integer.parseInt(seed, 16);
                    System.out.printf("\t\t%x\n", seedHex);
                    String basePath = BASE_PATH(config.getM(), config.getA0(), seedHex);
                    FileController.createFolderIfNotExists(basePath);
                    init(config, seedHex, basePath);
                    System.out.println("finished initialization");
                    List<Particle> particles = FileController.readParticlesState(basePath + "/particles.txt");
                    List<Obstacle> obstacles = FileController.readObstaclesState(basePath + "/obstacles.txt");
                    Field f = new Field(particles, obstacles);
                    System.out.println("Starting " + seedHex);
                    f.loop(particles, obstacles, basePath);
                }
            }
        }
    }


    public static void init(Config config, int seed, String basePath) {

        FileController.createFolderIfNotExists(basePath);
        List<Particle> particles = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();
        Random rnd = new Random(seed);

        double EPSILON = 1e-6f;

        double L = config.getL();
        double W = config.getW();

        int offset = 0;
        PeriodicGrid grid = new PeriodicGrid();
        // Create particles
        for (int i = 0; i < config.getN(); i++) {
            Particle p;
            boolean overlap;
            do {
                p = new Particle(i+offset, rnd.nextDouble() * (L - 2*config.getR()) + config.getR(), rnd.nextDouble() * (W - 2*config.getR()) + config.getR(),
                        config.getR(), config.getMASS());
                Particle p2 = p;
                List<Obstacle> collisions = grid.getSingleEntityCollisions(p);
                overlap = collisions.stream().anyMatch(e -> e.distance(p2) <= e.getR() + p2.getR() + EPSILON);
            } while(overlap);
            particles.add(p);
            grid.addEntity(p);
        }

        // Create obstacles
        for (int i = 0; i < config.getM(); i++) {
            Obstacle o;
            boolean overlap;
            do {
                o = new Obstacle(i+offset, rnd.nextDouble() * (L - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(), rnd.nextDouble() * (W - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(),
                        config.getOBSTACLE_RADIUS());
                Obstacle o2 = o;
                List<Obstacle> collisions = grid.getSingleEntityCollisions(o);
                overlap = collisions.stream().anyMatch(particle -> o2.distance(particle) <= particle.getR() + o2.getR() + EPSILON);
            } while(overlap);
            obstacles.add(o);
            grid.addEntity(o);
        }

        FileController.writeParticlesState(basePath + "/particles.txt", particles, false);
        FileController.writeObstacleState(basePath + "/obstacles.txt", obstacles, false);
    }

}