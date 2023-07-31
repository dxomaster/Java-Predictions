package engine;
public interface Engine {
    public void runSimulation();
    public void loadSimulationParametersFromFile(String filename);
    public void viewSimulationParameters();
    public void viewOldSimulationRuns();
    public void viewSingleSimulationRun(String runId);
}
