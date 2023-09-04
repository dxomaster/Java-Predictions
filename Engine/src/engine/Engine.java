package engine;

import DTO.*;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDWorld;
import world.World;
import world.utils.Property;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Engine implements Serializable {
    int counter = 0;
    String simulationName;
    private List<RunEndDTO> pastSimulationArtifactDTO = new ArrayList<>();
    private Map<String, World> pastSimulationWorlds = new HashMap<>();
    ExecutorService executorService;
    private PRDWorld template;
    private World world;
    Map<String, World> worlds = new HashMap<>();

    public void runSimulation() throws ErrorException, WarnException {
        World worldToRun = new World(this.world);
        String UUID = java.util.UUID.randomUUID().toString();
        this.worlds.put(UUID, worldToRun);
        this.executorService.submit(worldToRun);
    }

    public boolean isSimulationLoaded() {
        return world != null;
    }

    public RunStatisticsDTO getPastSimulationArtifactDTO(String uuid) throws ErrorException {
        if (pastSimulationWorlds.containsKey(uuid)) {
            World world = pastSimulationWorlds.get(uuid);
            //Map<String, List<EntityDTO>> entitiesDTO = createWorldEntitiesDTO(world.getEntities());
            List<StatisticEntityDTO> entityDefinitionDTO = createEntityDefinitionDTO(world);
            return new RunStatisticsDTO(entityDefinitionDTO);
        }
        throw new ErrorException("No such simulation");
    }

    private List<StatisticEntityDTO> createEntityDefinitionDTO(World world) {
        List<StatisticEntityDTO> entityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : world.getEntityDefinitionMap().values()) {
            List<StatisticPropertyDTO> propertyDTOList = new ArrayList<>();
            for (Property property : entityDefinition.getProperties().values()) {

                List<Entity> entityList = world.getEntities().get(entityDefinition.getName());
                StatisticPropertyDTO statisticPropertyDTO = createPropertyDTO(property, entityList);
                propertyDTOList.add(statisticPropertyDTO);
            }
            entityDTOList.add(new StatisticEntityDTO(propertyDTOList, entityDefinition.getName(), entityDefinition.getPopulation(), entityDefinition.getFinalPopulation()));
        }
        return entityDTOList;
    }

    private StatisticPropertyDTO createPropertyDTO(Property property, List<Entity> entityList) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (Entity entity : entityList) {
            Property prop = entity.getPropertyByName(property.getName());
            if (frequencyMap.containsKey(prop.getValue().toString())) {
                Integer value = frequencyMap.get(prop.getValue().toString());

                frequencyMap.put(prop.getValue().toString(), value + 1);
            } else
                frequencyMap.put(prop.getValue().toString(), 1);
        }
        return new StatisticPropertyDTO(property.getName(), property.getType().propertyClass.getSimpleName(), frequencyMap);

    }

    public void setEnvVariableWithDTO(PropertyDTO propertyDTO) throws WarnException {
        setEnvVariable(propertyDTO.getName(), propertyDTO.getValue());
    }

    private void setEnvVariable(String name, Object value) throws WarnException {
        if (!isSimulationLoaded())
            throw new IllegalArgumentException("No file is loaded");
        for (Property property : world.getEnvironmentVariables()) {
            if (property.getName().equals(name)) {
                property.setValue(value,0);
                return;
            }
        }
        throw new IllegalArgumentException("No such property");
    }

    public List<EnvDTO> getRequiredEnvDTO() {
        if (!isSimulationLoaded())
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

    public String getSimulationName() {
        return simulationName;
    }

    public void loadSimulationParametersFromFile(String filename) throws ErrorException {
        try {
            if(++counter > 1)
                counter = 3;
            this.pastSimulationArtifactDTO = new ArrayList<>();

            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.template = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            try {
                int threadAmount = this.template.getPRDThreadCount();
                this.executorService = Executors.newFixedThreadPool(threadAmount);
            }
            catch (Exception e){
                throw new ErrorException("Error in parsing number of threads, make sure it is a positive integer.");
            }

            this.world = new World(template);
            this.simulationName = filename;

        } catch (Exception e) {
            throw new ErrorException("Error in loading file please make sure this is a valid XML file with the correct schema:\n" + e.getMessage());
        }
    }

    public WorldDTO getSimulationParameters() {
        if (!isSimulationLoaded())
            throw new IllegalArgumentException("No file is loaded");
        return world.getWorldDTO();
    }

    public List<RunEndDTO> getPastArtifacts() {
        return pastSimulationArtifactDTO;
    }

    public void saveEngineToFile(String filename) throws ErrorException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(filename + ".save")))) {
            out.writeObject(this);
            out.flush();

        } catch (Exception e) {
            throw new ErrorException("Error in saving file.");
        }
    }

    public void loadEngineFromFile(String filenameToLoad) throws ErrorException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filenameToLoad)))) {
            // we know that we read array list of Persons
            Engine engine = (Engine) in.readObject();
            this.pastSimulationArtifactDTO = engine.pastSimulationArtifactDTO;
            this.pastSimulationWorlds = engine.pastSimulationWorlds;
            this.simulationName = engine.simulationName;
            this.world = engine.world;


        } catch (Exception e) {
            throw new ErrorException("Error in loading file, please make sure the file exists and has the correct format.");
        }
    }

    public void updateEntityPopulation(String name, Integer newValue) {
        if (world == null)
            throw new IllegalArgumentException("No file is loaded");
        world.updateEntityPopulation(name, newValue);
    }
}

