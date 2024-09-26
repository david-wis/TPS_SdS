import java.util.List;

public class Config {
    private List<Double> dts;
    private double m;
    private double k;
    private double g;
    private double tf;
    private double r0;

    public List<Double> getDts() {
        return dts;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public double getR0() {
        return r0;
    }

    public void setR0(double r0) {
        this.r0 = r0;
    }

}
