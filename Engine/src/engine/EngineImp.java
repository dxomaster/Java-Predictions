package engine;

import DTO.EnvDTO;
import DTO.SimulationArtifactDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.jaxb.schema.generated.PRDWorld;
import engine.world.World;
import engine.world.utils.Property;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineImp implements engine.Engine {
    private final Map<String, World> pastSimulationWorlds = new HashMap<>();
    private World world;

    @Override
    public List<EnvDTO> getEnvDTO() {
        return null;

    }

    @Override
    public SimulationArtifactDTO runSimulation() throws ErrorException {

        SimulationArtifactDTO finishedArtifact = world.run();

        pastSimulationWorlds.put(finishedArtifact.getUUID(), world);
        world = null;
        return finishedArtifact;
    }

    public void setEnvVariableWithDTO(EnvDTO envDTO) throws WarnException {
        if (world == null)
            throw new IllegalArgumentException("No file is loaded");
        for (Property property : world.getEnvironmentVariables()) {
            if (property.getName().equals(envDTO.getName())) {
                property.setValue(envDTO.getValue());
                return;
            }
        }
        throw new IllegalArgumentException("No such property");
    }

    public List<EnvDTO> getRequiredEnvDTO() {
        if (world == null)
            throw new IllegalArgumentException("No file is loaded");
        List<EnvDTO> requiredEnvDTO = new ArrayList<>();
        for (Property property : world.getEnvironmentVariables()) {
            if (property.getRange() != null)
                requiredEnvDTO.add(new EnvDTO(property.getName(), property.getType().propertyClass, property.getValue()
                        , property.getRange().getFrom(), property.getRange().getTo()));
            else
                requiredEnvDTO.add(new EnvDTO(property.getName(), property.getType().propertyClass, property.getValue()
                        , null, null));
        }
        return requiredEnvDTO;
    }

    @Override
    public void loadSimulationParametersFromFile(String filename) throws ErrorException {
        try {

            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PRDWorld jaxbWorld = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            this.world = new World(jaxbWorld);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void viewSimulationParameters() {
        if (world == null)
            throw new IllegalArgumentException("No file is loaded");
        System.out.println(world);//todo change to WorldDTO
    }

    @Override
    public void viewOldSimulationRuns() {


    }

    @Override
    public void viewSingleSimulationRun(String runId) {

    }
}

