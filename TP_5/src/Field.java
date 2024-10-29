import java.util.ArrayList;
import java.util.List;

public class Field {

    public Field(List<Particle> particles, List<Obstacle> obstacles) {
        PeriodicGrid grid = new PeriodicGrid();
        for (Particle p : particles) {
            grid.addEntity(p);
            p.setGrid(grid);
        }
        for (Obstacle o : obstacles) {
            grid.addEntity(o);
        }
        grid.updateCollisionMap(particles, obstacles);
    }

    public void loop(List<Particle> particles, List<Obstacle> obstacles, String BASE_PATH) {
        String animationPath = BASE_PATH + "/animation.txt";
        String analysisPath = BASE_PATH + "/analysis.txt";
        Config config = Config.getConfig();
        double t = config.getDT();
        double t2 = t;
        System.out.println(config.getDT());
        FileController.writeAnalysis(analysisPath, particles, 0, false);
        if (config.isANIMATION())
            FileController.writeParticlesState(animationPath, particles, false);

        while(t < config.getT()) {
            PeriodicGrid nextGrid = new PeriodicGrid();
            obstacles.forEach(nextGrid::addEntity);
            List<Particle> crossingParticles = new ArrayList<>();
            for(Particle p : particles) {
                double currX = p.getX();
                p.getIntegratorX().updatePrediction(t, config.getDT());
                if (currX > p.getX() + config.getL()/2)
                    crossingParticles.add(p);

                p.getIntegratorY().updatePrediction(t, config.getDT());
                nextGrid.addEntity(p);
                p.setGrid(nextGrid);
            }
            nextGrid.updateCollisionMap(particles, obstacles);
            for(Particle p : particles) {
                p.getIntegratorX().updateCorrection(t, config.getDT());
                p.getIntegratorY().updateCorrection(t, config.getDT());
            }

            if (!crossingParticles.isEmpty())
                FileController.writeAnalysis(analysisPath, crossingParticles, t, true);

            //serializacion
            if (t >= t2) {
                if (config.isANIMATION())
                    FileController.writeParticlesState(animationPath, particles, true);
                t2 += config.getDT2();

            }
            System.out.println(t + ": " + particles.get(0));
            t += config.getDT();
        }
    }
}
