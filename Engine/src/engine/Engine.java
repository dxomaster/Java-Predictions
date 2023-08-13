package engine;

import DTO.EnvDTO;
import DTO.SimulationArtifactDTO;
import Exception.ERROR.ErrorException;

import java.util.List;

public interface Engine {
    List<EnvDTO> getEnvDTO();

    SimulationArtifactDTO runSimulation() throws ErrorException;

    void loadSimulationParametersFromFile(String filename) throws ErrorException;

    void viewSimulationParameters();

    void viewOldSimulationRuns();

    void viewSingleSimulationRun(String runId);

}