package servletutils;

import DTO.WorldDTO;
import Exception.ERROR.ErrorException;
import engine.Engine;
import engine.jaxb.schema.generated.PRDWorld;
import world.World;

import java.util.HashMap;

public class SimulationManager {
    HashMap<String, PRDWorld> simulationDefinitionMap;
    Engine engine = new engine.Engine();


    public SimulationManager() {
        simulationDefinitionMap = new HashMap<>();
    }
    public void addSimulation(String simulation, String simulationName) throws ErrorException {
        engine.loadSimulationFromString(simulation, simulationName);
        System.out.println("world: " + engine.getCurrentWorldTemplate());
        simulationDefinitionMap.put(simulationName, engine.getXmlFileTemplate());
    }
    public void removeSimulation(String simulationName) {
        simulationDefinitionMap.remove(simulationName);
    }
    public PRDWorld getSimulationDefinition(String simulationName) {
        return simulationDefinitionMap.get(simulationName);
    }
    public boolean isSimulationExists(String simulationName) {
        return simulationDefinitionMap.containsKey(simulationName);
    }
    public WorldDTO getSimulationDetails(String simulationName) throws ErrorException {
        PRDWorld simulationDefinition = simulationDefinitionMap.get(simulationName);
        if(simulationDefinition == null) {
            throw new ErrorException("Simulation name " + simulationName + " does not exist");
        }
        WorldDTO simulationDetails =  this.engine.getSimulationParametersFromPRDWorld(simulationDefinition);
        return simulationDetails;
    }



}
