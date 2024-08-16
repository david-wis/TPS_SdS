import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        float MAX_RADIUS = 0.25f;
        int N = 2500;
        float L = 20;
        float rc = 1;

//        int M = 13;
        int M = (int) Math.floor(L / (rc + 2 * MAX_RADIUS));


        if (L/M <= rc + 2 * MAX_RADIUS)
            throw new IllegalArgumentException("The cell size must be greater than the interaction radius");

//        Grid grid = new Grid(N, L, M, rc);
        Grid grid = new PeriodicGrid(N, L, M, rc);
        List<Particle> particles = new ArrayList<>();

//        Particle p0 = new Particle(0, 19.9f, 19.8f, MAX_RADIUS);
//        grid.addParticle(p0);
//        particles.add(p0);
        int seed = 0xCAC71;
        Random rnd = new Random(seed);

        for (int i = 0; i < N; i++) {
//            Particle p = new Particle(i, rnd.nextFloat() * L,  rnd.nextFloat() * L, rnd.nextFloat() * MAX_RADIUS); // With random radius
            Particle p = new Particle(i, rnd.nextFloat() * L,  rnd.nextFloat() * L, MAX_RADIUS);
            grid.addParticle(p);
            particles.add(p);
        }
        FileController.writeParticlesState("initial_state.txt", particles);

        long start = System.currentTimeMillis();
        boolean[][] adjacencyMatrix = grid.getParticlesInRadiusForAll(particles);
        long finish = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (finish - start) + " ms");

        FileController.writeCIMOutput("output.txt", adjacencyMatrix, N, grid);
    }
}