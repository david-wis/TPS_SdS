import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int N = 1000;
        float L = 20;
        int M = 5;
        float rc = 1;

        if (L/M <= rc)
            throw new IllegalArgumentException("The cell size must be greater than the interaction radius");

        Grid grid = new Grid(N, L, M, rc);
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Particle p = new Particle(i, (float) Math.random() * L, (float) Math.random() * L, 0, 0);
            grid.addParticle(p);
            particles.add(p);
        }
        FileController.writeParticlesState("initial_state.txt", particles);
        boolean[][] adjacencyMatrix = grid.getParticlesInRadiusForAll(particles);

        FileController.writeCIMOutput("output.txt", adjacencyMatrix, N, grid);
    }
}