import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String STATE_PATH = "output/initial_state.txt";

    public static void main(String[] args) {
        Config config = FileController.getConfig();
        Obstacle obs = new Obstacle(config.getL()/2, config.getL()/2, config.getOBSTACLE_RADIUS());

        initParticles(config, obs);
        System.out.println("finished initialization");
        List<Particle> particles = FileController.readParticlesState(STATE_PATH);
        Field f = new Field(config.getL(), particles, obs);
        f.loop(config.getTOTAL_TIME(), config.getINTERVAL());
    }


    public static void initParticles(Config config, Obstacle obstacle) {

        List<Particle> particles = new ArrayList<>();

        int seed = 0xCAC71;
        Random rnd = new Random(seed);

        float EPSILON = 1e-6f;

        for (int i = 0; i < config.getN(); i++) {
            Particle p;
            boolean overlap;
            do {
                float vAngle = rnd.nextFloat() * 2 * (float) Math.PI;
                p = new Particle(i, rnd.nextFloat() * (config.getL() - 2*config.getR()) + config.getR(), rnd.nextFloat() * (config.getL() - 2*config.getR()) + config.getR(),
                        (float) Math.sin(vAngle) * config.getV(), (float) Math.cos(vAngle) * config.getV(), config.getR(), config.getM());
                Particle p2 = p;

                boolean overlapWithObstacle = obstacle.distance(p) <= EPSILON;
                overlap = overlapWithObstacle || particles.stream().anyMatch(particle -> particle.distance(p2) <= EPSILON);
            } while(overlap);
            particles.add(p);
        }
        FileController.writeParticlesState(STATE_PATH, particles, false);
    }

}