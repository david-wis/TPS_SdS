public class Config {
    private float L;
    private int N;
    private float M;
    private float V;
    private float R;
    private float INTERVAL;
    private float TOTAL_TIME;

    private float DT;
    private float OBSTACLE_RADIUS;
    private boolean MOVING_OBSTACLE;


    public float getL() {
        return L;
    }

    public void setL(float l) {
        L = l;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public float getM() {
        return M;
    }

    public void setM(float m) {
        M = m;
    }

    public float getV() {
        return V;
    }

    public void setV(float v) {
        V = v;
    }

    public float getR() {
        return R;
    }

    public void setR(float r) {
        R = r;
    }

    public float getINTERVAL() {
        return INTERVAL;
    }

    public void setINTERVAL(float INTERVAL) {
        this.INTERVAL = INTERVAL;
    }

    public float getDT() {
        return DT;
    }

    public void setDT(float DT) {
        this.DT = DT;
    }

    public float getOBSTACLE_RADIUS() {
        return OBSTACLE_RADIUS;
    }

    public void setOBSTACLE_RADIUS(float OBSTACLE_RADIUS) {
        this.OBSTACLE_RADIUS = OBSTACLE_RADIUS;
    }

    public boolean isMOVING_OBSTACLE() {
        return MOVING_OBSTACLE;
    }

    public void setMOVING_OBSTACLE(boolean MOVING_OBSTACLE) {
        this.MOVING_OBSTACLE = MOVING_OBSTACLE;
    }

    public float getTOTAL_TIME() {
        return TOTAL_TIME;
    }

    public void setTOTAL_TIME(float TOTAL_TIME) {
        this.TOTAL_TIME = TOTAL_TIME;
    }
}
