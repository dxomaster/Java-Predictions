package engine;

import DTO.EnvDTO;

import java.util.List;

public interface Engine {
    List<EnvDTO> getEnvDTO();
    void runSimulation();

    void loadSimulationParametersFromFile(String filename);

    void viewSimulationParameters();

    void viewOldSimulationRuns();

    void viewSingleSimulationRun(String runId);
}
