import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static String BASE_PATH(int seed){
        return String.format("output/%x", seed);
    }


    public static void main(String[] args) {
        Config config = FileController.getConfig("config/config.json");
        int seed = 0xCAC71;
        init(config, seed);
        System.out.println("finished initialization");
        List<Particle> particles = FileController.readParticlesState(BASE_PATH(seed) + "/particles.txt");
        List<Obstacle> obstacles = FileController.readObstaclesState(BASE_PATH(seed) + "/obstacles.txt");
        Field f = new Field(particles, obstacles);
        System.out.println("Starting " + seed);
        f.loop(particles, obstacles, BASE_PATH(seed));
    }


    public static void init(Config config, int seed) {

        FileController.createFolderIfNotExists(BASE_PATH(seed));
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
            int tries = 0;
            do {
                p = new Particle(i+offset, rnd.nextDouble() * (L - 2*config.getR()) + config.getR(), rnd.nextDouble() * (W - 2*config.getR()) + config.getR(),
                        config.getR(), config.getMASS());
                Particle p2 = p;
                List<Obstacle> collisions = grid.getSingleEntityCollisions(p);
                overlap = collisions.stream().anyMatch(e -> e.distance(p2) <= e.getR() + p2.getR() + EPSILON);
                tries++;
            } while(overlap);
            particles.add(p);
            grid.addEntity(p);
        }

        // Create obstacles
        for (int i = 0; i < config.getM(); i++) {
            Obstacle o;
            boolean overlap;
            int tries = 0;
            do {
                o = new Obstacle(i+offset, rnd.nextDouble() * (L - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(), rnd.nextDouble() * (W - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(),
                        config.getOBSTACLE_RADIUS());
                Obstacle o2 = o;
                List<Obstacle> collisions = grid.getSingleEntityCollisions(o);
                overlap = collisions.stream().anyMatch(particle -> o2.distance(particle) <= particle.getR() + o2.getR() + EPSILON);
                tries++;
            } while(overlap);
            obstacles.add(o);
            grid.addEntity(o);
        }

        FileController.writeParticlesState(BASE_PATH(seed) + "/particles.txt", particles, false);
        FileController.writeObstacleState(BASE_PATH(seed) + "/obstacles.txt", obstacles, false);
    }

}