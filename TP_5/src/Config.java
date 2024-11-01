import java.util.List;

public class Config {
    private double L;
    private double W;
    private int N;
    private int M;
    private List<Integer> MS;
    private double MASS;
    private double R;
    private double TOTAL_TIME;
    private double A0;
    private List<Double> A0S;
    private List<String> SEEDS;
    private String SEED;

    private double DT;
    private double OBSTACLE_RADIUS;
    private double T;
    private double KN;

    private double G;
    private boolean ANIMATION;

    private double DT2;


    private boolean DEBUG;

    private static String PATH;

    private static Config config = null;

    public static Config getConfig() {
        if (config != null) return config;
        config = FileController.getConfig(PATH);
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

    public int getM() {
        return M;
    }

    public List<Integer> getMS() {
        return MS;
    }

    public double getR() {
        return R;
    }

    public double getDT() {
        return DT * Math.sqrt(getMASS()/getKN());
    }

    public double getOBSTACLE_RADIUS() {
        return OBSTACLE_RADIUS;
    }

    public double getTOTAL_TIME() {
        return TOTAL_TIME;
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

    public boolean isDEBUG() {
        return DEBUG;
    }

    public double getA0() {
        return A0;
    }

    public List<Double> getA0S() {
        return A0S;
    }

    public List<String> getSEEDS() {
        return SEEDS;
    }

    public void setM(int m) {
        M = m;
    }

    public void setA0(double a0) {
        A0 = a0;
    }


    public static void setPATH(String PATH) {
        Config.PATH = PATH;
    }
}
