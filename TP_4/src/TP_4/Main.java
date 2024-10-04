package TP_4;


public class Main {
    public static void main(String[] args) {
        runSim2();
    }

    private static void runSim1(){
        Config config = FileController.getConfig("config/config1.json");
        FileController.createFolderIfNotExists("output/1");
        for (double dt : config.getDts()) {
            System.out.println("Running with dt = " + dt + " " + String.format("%.1g", dt));
            DampedOscillator.run(config, dt);
        }
    }

    private static void runSim2(){
        Config config = Config.getConfig2();
        FileController.createFolderIfNotExists("output/2");
        int N = config.getN();
        double m = config.getM();
        for (double k : config.getKs()) {
            config.setK(k);
            System.out.println("\n\nStarting k: " + config.getK());
            for (double wp : config.getWs()){
                config.setW(wp * Math.PI * Math.sqrt(k/m) / (N));
                System.out.println("\nStarting w = " + wp + "√" + k + " = " + config.getW());
//                LinkedOscillator.run();
            }
        }
    }

}