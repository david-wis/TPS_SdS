import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {


    private static String STATE_PATH(int seed, String fileName){
        return "output/" + seed + "/" + fileName + ".txt";
    }


    public static void main(String[] args) {
        staticObstacles();
    }

    public static void staticObstacles() {
        Config config = FileController.getConfig("config/config.json");
        int seed = 0xCAC71;
        init(config, seed);
        System.out.println("finished initialization");
        List<Particle> particles = FileController.readParticlesState(STATE_PATH(seed, "particles"));
        List<Obstacle> obstacles = FileController.readObstaclesState(STATE_PATH(seed, "obstacles"));
//        Field f = new Field(config.getL(), config.getV(), particles, obs, BASE_PATH(config, seed));
//        f.loop(config.getTOTAL_TIME(), config.getINTERVAL());
    }


    public static void init(Config config, int seed) {
        FileController.createFolderIfNotExists("output/" + seed);
        List<Particle> particles = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();
        Random rnd = new Random(seed);

        double EPSILON = 1e-6f;

        double L = config.getL();
        double W = config.getW();

        int offset = 0;

        // Create particles
        for (int i = 0; i < config.getN(); i++) {
            Particle p;
            boolean overlap;
            do {
                p = new Particle(i+offset, rnd.nextDouble() * (L - 2*config.getR()) + config.getR(), rnd.nextDouble() * (W - 2*config.getR()) + config.getR(),
                        config.getR(), config.getM());
                Particle p2 = p;

                overlap = particles.stream().anyMatch(particle -> particle.distance(p2) <= EPSILON);
            } while(overlap);
            particles.add(p);
        }

        // Create obstacles
        for (int i = 0; i < config.getM(); i++) {
            Obstacle o;
            boolean overlap;
            do {
                o = new Obstacle(i+offset, rnd.nextDouble() * (L - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(), rnd.nextDouble() * (W - 2*config.getOBSTACLE_RADIUS()) + config.getOBSTACLE_RADIUS(),
                        config.getOBSTACLE_RADIUS());
                Obstacle o2 = o;
                overlap = particles.stream().anyMatch(particle -> o2.distance(particle) <= EPSILON) || obstacles.stream().anyMatch(obs -> obs.distance(o2) <= EPSILON);
            } while(overlap);
            obstacles.add(o);
        }

        FileController.writeParticlesState(STATE_PATH(seed, "particles"), particles, false);
        FileController.writeObstacleState(STATE_PATH(seed, "obstacles"), obstacles, false);
    }

}