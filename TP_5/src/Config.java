public class Config {
    private double L;
    private double W;
    private int N;
    private double M;
    private double V;
    private double R;
    private double INTERVAL;
    private double TOTAL_TIME;
    private double A0;
    private double DT;
    private double OBSTACLE_RADIUS;
    private double OBSTACLE_MASS;
    private double T;


    private static Config config = null;

    public static Config getConfig() {
        if (config != null) return config;
        config = FileController.getConfig("config/config.json");
        return config;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getW() {
        return W;
    }

    public void setW(double w) {
        W = w;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        V = v;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public double getINTERVAL() {
        return INTERVAL;
    }

    public void setINTERVAL(double INTERVAL) {
        this.INTERVAL = INTERVAL;
    }

    public double getDT() {
        return DT;
    }

    public void setDT(double DT) {
        this.DT = DT;
    }

    public double getOBSTACLE_RADIUS() {
        return OBSTACLE_RADIUS;
    }

    public void setOBSTACLE_RADIUS(double OBSTACLE_RADIUS) {
        this.OBSTACLE_RADIUS = OBSTACLE_RADIUS;
    }

    public double getTOTAL_TIME() {
        return TOTAL_TIME;
    }

    public void setTOTAL_TIME(double TOTAL_TIME) {
        this.TOTAL_TIME = TOTAL_TIME;
    }

    public double getOBSTACLE_MASS() {
        return OBSTACLE_MASS;
    }

    public void setOBSTACLE_MASS(double OBSTACLE_MASS) {
        this.OBSTACLE_MASS = OBSTACLE_MASS;
    }

    public double getA0() {
        return A0;
    }

    public void setA0(double a0) {
        A0 = a0;
    }

    public double getT() {
        return T;
    }

    public void setT(double t) {
        T = t;
    }
}
