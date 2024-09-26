public class Main {
    public static void main(String[] args) {
        Config config = FileController.getConfig("config/config.json");
        DampedOscilator.run(config);
    }
}