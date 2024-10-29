public class Config {
    private double L;
    private double W;
    private int N;
    private double M;

    private double MASS;
    private double R;
    private double TOTAL_TIME;
    private double A0;
    private double DT;
    private double OBSTACLE_RADIUS;
    private double T;
    private double KN;

    private double G;
    private boolean ANIMATION;

    private double DT2;


    private static Config config = null;

    public static Config getConfig() {
        if (config != null) return config;
        config = FileController.getConfig("config/config.json");
        return config;
    }

    public double getL() {
        return L;
    }

    public double getW() {
        return W;
    }

    public int getN() {
        return N;
    }

    public double getM() {
        return M;
    }

    public double getR() {
        return R;
    }

    public double getDT() {
        return DT * Math.sqrt(getM()/getKN());
    }

    public void setDT(double DT) {
        this.DT = DT;
    }

    public double getOBSTACLE_RADIUS() {
        return OBSTACLE_RADIUS;
    }

    public double getTOTAL_TIME() {
        return TOTAL_TIME;
    }

    public double getA0() {
        return A0;
    }

    public double getT() {
        return T;
    }

    public double getKN() {
        return KN;
    }

    public double getG() {
        return G;
    }

    public boolean isANIMATION() {
        return ANIMATION;
    }

    public double getDT2() {
        return DT2;
    }

    public double getMASS() {
        return MASS;
    }
}
