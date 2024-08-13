import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        float MAX_RADIUS = 0.2f;
        int N = 5000;
        float L = 20;
        int M = 5;
        float rc = 1;

        if (L/M <= rc + 2 * MAX_RADIUS)
            throw new IllegalArgumentException("The cell size must be greater than the interaction radius");

//        Grid grid = new Grid(N, L, M, rc);
        Grid grid = new PeriodicGrid(N, L, M, rc);
        List<Particle> particles = new ArrayList<>();

//        Particle p0 = new Particle(0, 19.9f, 19.8f, MAX_RADIUS);
//        grid.addParticle(p0);
//        particles.add(p0);

        for (int i = 0; i < N; i++) {
            Particle p = new Particle(i, (float) Math.random() * L, (float) Math.random() * L, (float) Math.random() * MAX_RADIUS);
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