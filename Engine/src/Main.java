public class Main {
    public static void main(String[] args) {

        Engine engine = new Engine();
        engine.loadSimulationParametersFromFile("ex1-cigarets.xml");
        engine.viewSimulationParameters();
        engine.runSimulation();

    }
}
