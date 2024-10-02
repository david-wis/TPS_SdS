package TP_4;

import java.util.List;

public class Config {
    private List<Double> dts; // Sist 1
    private double m; // Sist 1 & 2
    private double k; // Sist 1 & 2
    private double g; // Sist 1
    private double tf; // Sist 1 & 2
    private double r0; // Sist 1


    private double dt2; // Sist 2
    private double A; // Sist 2
    private double l0; // Sist 2
    private int N; // Sist 2
    private List<Double> ks;
    private List<Double> ws;
    private double w; // Sist 2

    private static Config config1, config2;

    public static Config getConfig1() {
        if (config1 != null) return config1;
        config1 = FileController.getConfig("config/config1.json");
        return config1;
    }

    public static Config getConfig2() {
        if (config2 != null) return config2;
        config2 = FileController.getConfig("config/config2.json");
        return config2;
    }

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

    public void setDts(List<Double> dts) {
        this.dts = dts;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getL0() {
        return l0;
    }

    public void setL0(double l0) {
        this.l0 = l0;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getDt2() {
        return dt2;
    }

    public void setDt2(double dt2) {
        this.dt2 = dt2;
    }

    public List<Double> getKs() {
        return ks;
    }

    public List<Double> getWs() {
        return ws;
    }
}
