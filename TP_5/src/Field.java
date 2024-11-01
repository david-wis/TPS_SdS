import java.util.*;

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
        if (config.isANIMATION())
            FileController.writeParticlesState(animationPath, particles, false);
        FileController.writeAnalysis(analysisPath, Collections.emptyList(), t, false);
        Map<Particle, Integer> particleLaps = new HashMap<>();
        particles.forEach(p -> particleLaps.put(p, 1));
        while(t < config.getT()) {
            PeriodicGrid nextGrid = new PeriodicGrid();
            obstacles.forEach(nextGrid::addEntity);
            List<Particle> crossingParticles = new ArrayList<>();

            for(Particle p : particles) {
                double currX = p.getX();
                double nextX = p.getIntegratorX().updatePrediction(t, config.getDT());
                double nextY = p.getIntegratorY().updatePrediction(t, config.getDT());

                p.setX(nextX);
                p.setY(nextY);
                int laps = particleLaps.get(p);
                if (p.getAccumX() > laps * config.getL()) {
                    particleLaps.put(p, laps + 1);
                    crossingParticles.add(p);
                }

                nextGrid.addEntity(p);
                p.setGrid(nextGrid);
            }
            nextGrid.updateCollisionMap(particles, obstacles);
            for(Particle p : particles) {
                double nextVx = p.getIntegratorX().updateCorrection(t, config.getDT());
                double nextVy = p.getIntegratorY().updateCorrection(t, config.getDT());
                p.setVx(nextVx);
                p.setVy(nextVy);
            }

            if (!crossingParticles.isEmpty())
                FileController.writeAnalysis(analysisPath, crossingParticles, t, true);

            if (t >= t2) {
                if (config.isANIMATION())
                    FileController.writeParticlesState(animationPath, particles, true);
                t2 += config.getDT2();

            }
//            System.out.println(t);
            t += config.getDT();
        }
    }
}
