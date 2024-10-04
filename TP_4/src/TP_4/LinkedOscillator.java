package TP_4;

import TP_4.Integrators.*;

import java.util.*;

public class LinkedOscillator {
   public static void run() {
       Config config = Config.getConfig2();
       int N = config.getN();
       double dt = 1 / (config.getW() * 100);
       double dt2 = config.getDt2();
       double tf = config.getTf();
       String BASE_PATH = "output/2/" + (int) config.getK();
       FileController.createFolderIfNotExists(BASE_PATH);
       BASE_PATH += "/" + config.getW();
       FileController.createFolderIfNotExists(BASE_PATH);
       String animationPath = BASE_PATH + "/animation.txt";
       FileController.createEmptyFile(animationPath);
       String maxPath = BASE_PATH + "/max.txt";
       FileController.createEmptyFile(maxPath);

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

       while (t <= tf) {
           particlesCopy[N].r = config.getA() * Math.sin(config.getW() * t);
           for(int i = 1; i < N; i++) {
                integrators[i].update(t, dt);
           }

           for(int i = 1; i < N; i++) {
               particlesCopy[i] = (LinkedParticle) integrators[i].getParticle().copy();
           }

           if (t >= t2) {
               FileController.writeParticlesState(animationPath, Arrays.asList(particlesCopy), t, true);
               Particle maxParticle = Arrays.stream(integrators).filter(Objects::nonNull)
                       .max((i1, i2) -> Math.abs(i1.getParticle().getR()) > Math.abs(i2.getParticle().getR()) ? 1 : -1)
                       .map(i -> i.getParticle()).get();
               FileController.writeParticlesState(maxPath, maxParticle, t, true);
//               System.out.println(t + " " + t2);
               t2 += dt2;
           }
           t += dt;
       }
   }
}
