package TP_4;

import TP_4.Integrators.GPC;

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

       GPC[] integrators = new GPC[N+1];
       LinkedParticle[] particlesCopy = new LinkedParticle[N+1];
       particlesCopy[0] = new LinkedParticle(0, null);
       for (int i = 1; i < N; i++) {
          LinkedParticle p = new LinkedParticle(i, particlesCopy);
          particlesCopy[i] = (LinkedParticle) p.copy();
          integrators[i] = new GPC(p, 0,0,0,0,0,0);
       }
       LinkedParticle lastParticle = new LinkedParticle(N, particlesCopy);
       particlesCopy[N] = (LinkedParticle) lastParticle.copy();
       integrators[N] = initializeGPC(lastParticle);

       double t = dt;
       double t2 = t;
       while (t <= tf) {
           for(int i = 1; i <= N; i++) {
                integrators[i].update(t, dt);
           }
           for(int i = 1; i <= N; i++) {
               particlesCopy[i] = (LinkedParticle) integrators[i].getParticle().copy();
           }
           FileController.writeParticlesState(statePath, particlesCopy[N], t, true);

           if (t >= t2) {
               FileController.writeParticlesState(animationPath, Arrays.asList(particlesCopy), t, true);
               t2 += dt2;
//               System.out.println(t + " " + t2);
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
       double r5 = 0;
       return new GPC(p, r, v, a, r3, r4, r5);
    }
}
