package TP_4;

import TP_4.Integrators.*;

import java.util.Arrays;

public class LinkedOscillator {
   public static void run() {
       Config config = Config.getConfig2();
       int N = config.getN();
       double dt = 1 / (config.getW() * 100);
       double dt2 = config.getDt2();
       double tf = config.getTf();

       String statePath = "output/2/state.txt";
       String animationPath = "output/2/animation.txt";
       FileController.createEmptyFile(statePath);
       FileController.createEmptyFile(animationPath);

       Integrator[] integrators = new Verlet[N+1];
       LinkedParticle[] particlesCopy = new LinkedParticle[N+1];
       particlesCopy[0] = new LinkedParticle(0, null);
       LinkedParticle lastParticle = new LinkedParticle(N, null);
       particlesCopy[N] = (LinkedParticle) lastParticle.copy();

       for (int i = 1; i < N ; i++) {
          LinkedParticle p = new LinkedParticle(i, particlesCopy);
          particlesCopy[i] = (LinkedParticle) p.copy();
          integrators[i] = new Verlet(p, 0);
       }


       double t = 2*dt;
       double t2 = t;
       double tStationary = 0;// Double.MAX_VALUE;
       double yMax = 0;
       double tMax = 0;
       while (t <= tf + tStationary) {
           particlesCopy[N].r = config.getA() * Math.sin(config.getW() * t);
           for(int i = 1; i < N; i++) {
                integrators[i].update(t, dt);
           }
           if (integrators[N/2].getParticle().getR() > yMax) {
               yMax = integrators[N/2].getParticle().getR();
               tMax = t;
           }
           if (tStationary == Double.MAX_VALUE
                   && Math.abs(integrators[N/2].getParticle().getR() - yMax) < 1e-6
                   && yMax != 0
                   && tMax > t) {
               tStationary = t;
               System.out.println("\n\nStationary at " + tStationary);
           }
           for(int i = 1; i < N; i++) {
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