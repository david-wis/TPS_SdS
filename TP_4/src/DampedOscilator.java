import Integrators.*;

import java.util.List;

public class DampedOscilator {
    public static void run(Config config, double DT) {

        double R0 = config.getR0();
        double V0 = -config.getR0() *config.getG() / (2 * config.getM());
        Particle1D p = new Particle1D(config.getM(), (double) DT, R0, config.getK(), config.getG());


        Verlet verlet = new Verlet(p.copy(), R0);
        Beeman beeman = new Beeman(p.copy(), (-config.getK() * R0 - config.getG() * V0) / config.getM());
        GPC gpc = initializeGPC(config, p, R0);
        List<Integrator> integrators = List.of(verlet, beeman, gpc);
        String basePath = "output/"  + String.format("%.1g",DT);
        FileController.createFolderIfNotExists(basePath);
        for (Integrator integrator : integrators) {
            System.out.println("Running " + integrator.getClass().getSimpleName() + " integrator");
            String filename = basePath + "/state_" + integrator.getClass().getSimpleName().toLowerCase() + ".txt";
            FileController.createEmptyFile(filename);
            FileController.writeParticlesState(filename, new Particle1D(R0, V0, config.getM()), 0, true);
            FileController.writeParticlesState(filename, integrator.getParticle(), DT, true);

            double t = 2 * DT;
            while (t <= config.getTf()) {
                integrator.update((double) DT, config.getK(), config.getG());
                FileController.writeParticlesState(filename, integrator.getParticle(), t, true);
                t += DT;

            }
        }
    }

    public static GPC initializeGPC(Config config, Particle1D p, double R0) {
        double k_m = config.getK() / config.getM();
        double g_m = config.getG() / config.getM();
        double a = p.getAcceleration(p.getR(), p.getV(), config.getK(), config.getG());
        double r3 = - k_m * p.getV() - g_m * a;
        double r4 = - k_m * a - g_m * r3;
        double r5 = - k_m * r3 - g_m * r4;
        return new GPC(p.copy(),
                p.getR(),
                p.getV(),
                a,
                r3,
                r4,
                r5);
    }
}
