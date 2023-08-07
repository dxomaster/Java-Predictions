import engine.factory.WorldFactory;

public class Main {
    public static void main(String[] args) {

        Engine engine = new Engine();
        engine.loadSimulationParametersFromFile("ex1-error-2.xml");
        engine.runSimulation();
        engine.viewSimulationParameters();
    }
}
