public class Main {
    public static void main(String[] args) {
        Config config = FileController.getConfig("config/config.json");
        for (double dt : config.getDts()) {
            System.out.println("Running with dt = " + dt + " " + String.format("%.1g", dt));
            DampedOscilator.run(config, dt);
        }

    }
}