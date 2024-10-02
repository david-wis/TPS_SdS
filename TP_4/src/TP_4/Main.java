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
        Config config = FileController.getConfig("config/config2.json");
        LinkedOscillator.run();
    }

    private static void runSim22(){
        Config config = FileController.getConfig("config/config2.json");
//        for (double w : config.getWs()) {
        // FileCotroller.
//        LinkedOscillator.run();
        //}
    }
}