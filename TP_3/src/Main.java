import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final int N = 201;
    private static final float L = 0.1f;
    private static final float RADIUS = 0.001f;
    private static final float OBS_RADIUS = 0.005f;

    private static final String STATE_PATH = "output/initial_state.txt";

    public static void main(String[] args) {
//        initParticles(N, L, new Obstacle(L/2, L/2, OBS_RADIUS));
        List<Particle> particles = FileController.readParticlesState(STATE_PATH);
        Field f = new Field(L, particles, new Obstacle(L/2, L/2, OBS_RADIUS));
        f.loop(10);
    }


    public static void initParticles(int N, float L, Obstacle obstacle) {
        final float r = 0.001f;
        final float speed = 1.0f;
        final float mass = 1;
        List<Particle> particles = new ArrayList<>();

//        Particle p0 = new Particle(0, 19.9f, 19.8f, MAX_RADIUS);
//        grid.addParticle(p0);
//        particles.add(p0);
        int seed = 0xCAC71;
        Random rnd = new Random(seed);

        for (int i = 0; i < N; i++) {
            Particle p;
            boolean overlap;
            do {
                float vAngle = rnd.nextFloat() * 2 * (float) Math.PI;
                p = new Particle(i, rnd.nextFloat() * (L - 2*r) + r, rnd.nextFloat() * (L - 2*r) + r, (float) Math.sin(vAngle) * speed, (float) Math.cos(vAngle) * speed, r, mass);
                Particle p2 = p;
                overlap = particles.stream().anyMatch(particle -> particle.distance(p2) <= 0) && obstacle.distance(p) > 0;
            } while(overlap);
            particles.add(p);
        }
        FileController.writeParticlesState(STATE_PATH, particles, false);
    }

}