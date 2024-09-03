import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


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
                p = new Particle(i, rnd.nextFloat() * L, rnd.nextFloat() * L, (float) Math.sin(vAngle) * speed, (float) Math.cos(vAngle) * speed, r, mass);
                Particle p2 = p;
                overlap = particles.stream().anyMatch(particle -> particle.distance(p2) <= 0) && obstacle.distance(p) > 0;
            } while(overlap);
            particles.add(p);
        }
        FileController.writeParticlesState("initial_state.txt", particles);
    }

}