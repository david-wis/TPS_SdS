import java.util.List;

public class Field {

    public Field(double L, double W, double V, List<Particle> particles, List<Obstacle> obstacles, String path) {
        PeriodicGrid grid = new PeriodicGrid(particles.size() + obstacles.size());
        for (Particle p : particles) {
            grid.addEntity(p);
            p.setGrid(grid);
        }
        for (Obstacle o : obstacles) {
            grid.addEntity(o);
        }
    }

    public void loop(List<Particle> particles, List<Obstacle> obstacles) {
        Config config = Config.getConfig();
        double t = config.getDT();
        while(t < config.getT()) {
            PeriodicGrid nextGrid = new PeriodicGrid(particles.size() + obstacles.size());
            obstacles.forEach(nextGrid::addEntity);
            for(Particle p : particles) {
                p.getIntegratorX().updatePrediction(t, config.getDT());
                p.getIntegratorY().updatePrediction(t, config.getDT());
                nextGrid.addEntity(p);
                p.setGrid(nextGrid);
            }
            nextGrid.updateCollisionMap(particles, obstacles);
            for(Particle p : particles) {
                p.getIntegratorX().updateCorrection(t, config.getDT());
                p.getIntegratorY().updateCorrection(t, config.getDT());
            }
            //serializacion
        }
    }
}
