import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static String BASE_PATH(Config config, int seed){
        if (config.isMOVING_OBSTACLE())
            return "output/moving/" + String.format("%x", seed);
        else
            return "output/" + (int) config.getV();
    }

    private static String STATE_PATH(Config config, int seed){
        return BASE_PATH(config, seed) + "/initial_state.txt";
    }


    public static void main(String[] args) {
        staticObstacle();
//        movingObstacle();
    }

    public static void staticObstacle(){
//        Config config = FileController.getConfig("config/animation_config.json");
        Config config = FileController.getConfig("config/static_config.json");
        Obstacle obs = null;
        if (!config.isMOVING_OBSTACLE())
            obs = new Obstacle(config.getL() / 2, config.getL() / 2, config.getOBSTACLE_RADIUS());
        int seed = 0xCAC71;
        initParticles(config, obs, seed);
        System.out.println("finished initialization");
        List<Particle> particles = FileController.readParticlesState(STATE_PATH(config, seed));
        Field f = new Field(config.getL(), config.getV(), particles, obs, BASE_PATH(config, seed));
        f.loop(config.getTOTAL_TIME(), config.getINTERVAL());
    }

    public static void movingObstacle(){
        Config config = FileController.getConfig("config/moving_config.json");
        Obstacle obs = null;
        if (!config.isMOVING_OBSTACLE())
            obs = new Obstacle(config.getL() / 2, config.getL() / 2, config.getOBSTACLE_RADIUS());
        for (int s : List.of(0xCAC71, 0xCAFEBABE, 0xDEADBEEF)){
            FileController.createFolderIfNotExists(BASE_PATH(config, s));
            initParticles(config, obs, s);
            System.out.println("finished initialization" + s);
            List<Particle> particles = FileController.readParticlesState(STATE_PATH(config, s));
            Field f = new Field(config.getL(), config.getV(), particles, obs, BASE_PATH(config, s));
            f.loop(config.getTOTAL_TIME(), config.getINTERVAL());
        }
    }


    public static void initParticles(Config config, Obstacle obstacle, int seed) {

        List<Particle> particles = new ArrayList<>();
        Random rnd = new Random(seed);

        float EPSILON = 1e-6f;

        float L = config.getL();

        int offset = 0;
        if (obstacle == null){
            Particle o = new Particle(0, L / 2, L / 2, 0, 0, config.getOBSTACLE_RADIUS(), config.getOBSTACLE_MASS());
            particles.add(o);
            offset = 1;
        }

        for (int i = 0; i < config.getN(); i++) {
            Particle p;
            boolean overlap;
            do {
                float vAngle = rnd.nextFloat() * 2 * (float) Math.PI;
                p = new Particle(i+offset, rnd.nextFloat() * (L - 2*config.getR()) + config.getR(), rnd.nextFloat() * (L - 2*config.getR()) + config.getR(),
                        (float) Math.sin(vAngle) * config.getV(), (float) Math.cos(vAngle) * config.getV(), config.getR(), config.getM());
                Particle p2 = p;

                boolean overlapWithObstacle = obstacle != null && obstacle.distance(p) <= EPSILON;
                overlap = overlapWithObstacle || particles.stream().anyMatch(particle -> particle.distance(p2) <= EPSILON);
            } while(overlap);
            particles.add(p);
        }

        FileController.writeParticlesState(STATE_PATH(config, seed), particles, false);
    }

}