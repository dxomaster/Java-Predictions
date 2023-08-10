package engine;

public interface Engine {
    void runSimulation();

    void loadSimulationParametersFromFile(String filename);

    void viewSimulationParameters();

    void viewOldSimulationRuns();

    void viewSingleSimulationRun(String runId);
}
