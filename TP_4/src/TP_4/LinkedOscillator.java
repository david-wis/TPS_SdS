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
       double tStationary = Double.MAX_VALUE;// Double.MAX_VALUE;
       double yMax = 0;
       double tMax = 0;
       int countMax = 0;
//       List<Double> maxes = new ArrayList<>();
       while (t <= tf + tStationary) {
           particlesCopy[N].r = config.getA() * Math.sin(config.getW() * t);
           for(int i = 1; i < N; i++) {
                integrators[i].update(t, dt);
           }
           double max = Arrays.stream(integrators).filter(Objects::nonNull).map(i -> i.getParticle().getR()).max((o1, o2) -> Math.abs(o1) > Math.abs(o2) ? 1 : -1).get();
//           maxes.add(max);

//           if (maxes.size() == 100000) {
//               System.out.println("Max (" + t + "): " + maxes.stream().max((o1, o2) -> Math.abs(o1) > Math.abs(o2) ? 1 : -1).get());
//               maxes.clear();
//           }

           if (Math.abs(max) > Math.abs(yMax)) {
               yMax = max;
               tMax = t;
               countMax = 0;
           } else if (Math.abs(max - yMax) < 1e-3){
               countMax++;
           }

           if (tStationary == Double.MAX_VALUE
                   && countMax == 100
                   && yMax != 0
                   && tMax < t) {
               tStationary = t;
               System.out.println("\n\nStationary at " + tStationary + "with yMax = " + yMax);
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
}
