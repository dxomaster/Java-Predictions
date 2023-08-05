import engine.factory.WorldFactory;

public class Main {
    public static void main(String[] args) {

        Engine engine = new Engine();
        engine.loadSimulationParametersFromFile("master-ex1.xml");
        engine.runSimulation();
        //engine.runSimulation();
    }
}
