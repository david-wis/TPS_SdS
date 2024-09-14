import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final int N = 250;
    private static final float L = 0.1f;
    private static final float RADIUS = 0.001f; // 0.02f; // 0.001
    private static final float OBS_RADIUS = 0.005f;
    private static final float SPEED = 1.0f;
    private static final float MASS = 1.0f;

    private static final String STATE_PATH = "output/initial_state.txt";

    public static void main(String[] args) {
        initParticles(N, L, new Obstacle(L/2, L/2, OBS_RADIUS));
        System.out.println("finished initialization");
        List<Particle> particles = FileController.readParticlesState(STATE_PATH);
        Field f = new Field(L, particles, new Obstacle(L/2, L/2, OBS_RADIUS));
        f.loop(200f, 0.01f);
    }


    public static void initParticles(int N, float L, Obstacle obstacle) {

        List<Particle> particles = new ArrayList<>();

//        Particle p0 = new Particle(0, 19.9f, 19.8f, MAX_RADIUS);
//        grid.addParticle(p0);
//        particles.add(p0);
        int seed = 0xCAC71;
        Random rnd = new Random(seed);

        float EPSILON = 1e-6f;

        for (int i = 0; i < N; i++) {
            Particle p;
            boolean overlap;
            do {
                float vAngle = rnd.nextFloat() * 2 * (float) Math.PI;
                p = new Particle(i, rnd.nextFloat() * (L - 2*RADIUS) + RADIUS, rnd.nextFloat() * (L - 2*RADIUS) + RADIUS,
                        (float) Math.sin(vAngle) * SPEED, (float) Math.cos(vAngle) * SPEED, RADIUS, MASS);
                Particle p2 = p;

                boolean overlapWithObtacle = obstacle.distance(p) <= EPSILON;
                overlap = overlapWithObtacle || particles.stream().anyMatch(particle -> particle.distance(p2) <= EPSILON);// || obstacle.distance(p) <= 0;
            } while(overlap);
            particles.add(p);
        }
        FileController.writeParticlesState(STATE_PATH, particles, false);
    }

}