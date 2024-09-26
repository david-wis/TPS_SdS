import Integrators.Particle1D;
import Integrators.Verlet;

public class DampedOscilator {
    public static void run(Config config) {
        FileController.createEmptyFile("output/verlet_state.txt");
        Particle1D p = new Particle1D(config.getR0(), -config.getR0() *config.getGamma() / 2, config.getM());
        float r1 = (float) (config.getR0() * Math.exp(-config.getGamma() * config.getDt() / (2 * config.getM()) ) * Math.cos(Math.sqrt(config.getK() / config.getM() - Math.pow(config.getGamma() / (2 * config.getM()), 2)) * config.getDt()));
        Verlet verlet = new Verlet(p, p.getR(), r1);
        float t = config.getDt();
        while (t <= config.getTf()) {
            verlet.update(config.getDt(), config.getK(), config.getGamma());
            FileController.writeParticlesState("output/verlet_state.txt", p, true);
            t += config.getDt();
        }
    }
}
