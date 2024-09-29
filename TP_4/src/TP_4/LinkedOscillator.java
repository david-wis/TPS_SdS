package TP_4;

import TP_4.Integrators.Beeman;
import TP_4.Integrators.GPC;
import TP_4.Integrators.Verlet;

import java.io.File;
import java.util.Arrays;

public class LinkedOscillator {
   public static void run() {
       Config config = Config.getConfig2();
       int N = config.getN();
       double dt = config.getDt();
       double dt2 = config.getDt2();
       double tf = config.getTf();

       String statePath = "output/2/state.txt";
       String animationPath = "output/2/animation.txt";
       FileController.createEmptyFile(statePath);
       FileController.createEmptyFile(animationPath);

       Beeman[] integrators = new Beeman[N+1];
       LinkedParticle[] particlesCopy = new LinkedParticle[N+1];
       particlesCopy[0] = new LinkedParticle(0, null);
       for (int i = 1; i < N ; i++) {
          LinkedParticle p = new LinkedParticle(i, particlesCopy);
          particlesCopy[i] = (LinkedParticle) p.copy();
          integrators[i] = new Beeman(p, 0);
       }
       LinkedParticle lastParticle = new LinkedParticle(N, particlesCopy);
       particlesCopy[N] = (LinkedParticle) lastParticle.copy();
       lastParticle.setR(1.5 * dt * dt * config.getA());
       integrators[N] = new Beeman(lastParticle, config.getA());//initializeGPC(lastParticle);

       double t = dt;
       double t2 = t;
       double tStationary = Double.MAX_VALUE;
       while (t <= tf + tStationary) {
           for(int i = 1; i <= N; i++) {
                integrators[i].update(t, dt);
           }
           if (tStationary == Double.MAX_VALUE && integrators[1].getParticle().getR() != 0) {
               tStationary = t;
               System.out.println("\n\nStationary at " + tStationary);
           }
           for(int i = 1; i <= N; i++) {
               particlesCopy[i] = (LinkedParticle) integrators[i].getParticle().copy();
           }
//           FileController.writeParticlesState(statePath, particlesCopy[N], t, true);

           if (t >= t2) {
               if (t >= tStationary) {
                   FileController.writeParticlesState(animationPath, Arrays.asList(particlesCopy), t, true);
               }
               System.out.println(t + " " + t2);
               t2 += dt2;
           }
           t += dt;
       }
   }

    public static GPC initializeGPC(Particle p) {
       Config config = Config.getConfig2();
       double A = config.getA();
       double w = config.getW();

       double r = 0;
       double v = 0;
       double a = A; // A * cos(w * 0)
       double r3 = 0; // -A * w * sen(w * 0)
       double r4 = -A * w * w; // -A * w^2 * cos(w * 0)
       double r5 = 0; // A w^3 sen(w * 0)
       return new GPC(p, r, v, a, r3, r4, r5);
    }

//    public static GPC initializeGPC2(Particle p) {
//        Config config = Config.getConfig2();
//        double A = config.getA();
//        double k = config.getK();
//        double m = config.getM();
//        double k_m = k / m;
//
//        double r = A;
//        double v = 0;
//        double a = -k_m * config.getA(); // A * cos(w * 0)
//        double r3 = 0; // -A * w * sen(w * 0)
//        double r4 = k_m * k_m * A; // -A * w^2 * cos(w * 0)
//        double r5 = 0; // A w^3 sen(w * 0)
//        return new GPC(p, r, v, a, r3, r4, r5);
//    }
}
