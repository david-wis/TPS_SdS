
public class Beeman{
    double aPrev;
    Particle.ParticleProjection p;
    double vCurr, aCurr, vpNext;

    public Beeman(Particle.ParticleProjection p, double a0) {
        this.p = p;
        this.aPrev = a0;
    }

    public double updatePrediction(double t, double dt){
        double rCurr = p.getR();
        vCurr = p.getV();
        aCurr = p.getA(true);
        double rNext = rCurr + vCurr * dt + ((2.0/3) * aCurr - (1.0/6) * aPrev) * dt * dt;
        vpNext = vCurr + (3.0/2) * aCurr * dt - (1.0/2) * aPrev * dt;
        return rNext;
//        p.setR(rNext);
    }


    public double updateCorrection(double t, double dt) {
        double acNext = p.getA(false);
        double vcNext = vCurr + (1.0/3) * acNext * dt + (5.0/6) * aCurr * dt - (1.0/6) * aPrev * dt;
//        p.setV(vcNext);
        aPrev = aCurr;
        return vcNext;
    }
}
