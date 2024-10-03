package TP_4;


public class Main {
    public static void main(String[] args) {
        runSim2();
    }

    private static void runSim1(){
        Config config = FileController.getConfig("config/config1.json");
        for (double dt : config.getDts()) {
            System.out.println("Running with dt = " + dt + " " + String.format("%.1g", dt));
            DampedOscillator.run(config, dt);
        }
    }

    private static void runSim2(){
        Config config = Config.getConfig2();
        FileController.createFolderIfNotExists("output/2");
        for (double k : config.getKs()) {
            config.setK(k);
            System.out.println("\n\nStarting k: " + config.getK());
            for (double w : config.getWs()){
                config.setW(Math.sqrt(k) * w);
                System.out.println("\nStarting w = " + w + "√" + k + " = " + config.getW());
                LinkedOscillator.run();
            }
        }
    }

}