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
            double dw = config.getDws().get((int) k + "");
            System.out.println("\n\nStarting k: " + config.getK());
            for (int offset=-config.getSteps()/2 - 1; offset <= config.getSteps()/2 + 1; offset++){
                config.setW(Math.sqrt(k) + dw * offset);
                System.out.println("\nStarting w = " + Math.sqrt(k) + "+" + offset + " * " + dw + " * " + " = " + config.getW());
                LinkedOscillator.run();
            }
        }
    }

}