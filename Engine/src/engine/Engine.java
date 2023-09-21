package engine;

import DTO.*;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDWorld;
import javafx.beans.property.IntegerProperty;
import world.World;
import world.utils.Property;
import world.utils.PropertyType;

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
import java.util.concurrent.ThreadPoolExecutor;

public class Engine implements Serializable {
    String simulationName;
    private Map<String, World> pastSimulationWorlds = new HashMap<>();
    private ThreadPoolExecutor executorService;
    private PRDWorld template;
    private World world;
    Map<String, World> worlds = new HashMap<>();
    public void clearPastSimulations() {
        worlds.clear();
    }
    public WorldDTO getWorldDTOByUUID(String uuid) {
        return worlds.get(uuid).getWorldDTO();
    }
    public void runSimulation() throws ErrorException, WarnException {
        int rows = this.template.getPRDGrid().getRows();
        int columns = this.template.getPRDGrid().getColumns();
        int maxEntities = rows * columns;
        if (getTotalPopulationOfEntities() > maxEntities)
            throw new ErrorException("Total population of entities is greater than the grid size");

        World worldToRun = new World(this.world);
        String UUID = java.util.UUID.randomUUID().toString();
        this.worlds.put(UUID, worldToRun);
        this.executorService.submit(worldToRun);
        System.out.println("Simulation is running");
    }
    private int getTotalPopulationOfEntities()
    {
        int sum = 0;
        for (EntityDefinition entityDefinition : world.getEntityDefinitionMap().values()) {
            sum += entityDefinition.getPopulation();
        }
        return sum;
    }
    public synchronized void pause(String uuid)  {
        if (worlds.containsKey(uuid)) {

            World world = worlds.get(uuid);
            if(!world.isRunning())
                throw new RuntimeException("Simulation is not running");
            world.setPaused(true);
        }
    }
    public int getAmountOfIdleThreads() {
        return ((ThreadPoolExecutor) executorService).getQueue().size();
    }
    public int getAmountOfRunningThreads() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }
    public synchronized void stop(String uuid) {
        if (worlds.containsKey(uuid)) {

            World world = worlds.get(uuid);
            if(!world.isRunning())
                throw new RuntimeException("Simulation is not running");
            world.setPaused(false);
            world.setStopped(true);
        }
    }

    public synchronized void resume(String uuid) {
        if (worlds.containsKey(uuid)) {
            World world = worlds.get(uuid);
            if(!world.isRunning())
                throw new RuntimeException("Simulation is not running");
            world.setPaused(false);
        }
    }

    public void clearExecution() throws ErrorException {
        this.world = new World(template);
    }

    public boolean isSimulationLoaded() {
        return world != null;
    }

    public RunStatisticsDTO getPastSimulationStatisticsDTO(String uuid) throws ErrorException {
        if (worlds.containsKey(uuid)) {
            World world = worlds.get(uuid);
            //Map<String, List<EntityDTO>> entitiesDTO = createWorldEntitiesDTO(world.getEntities());
            List<StatisticEntityDTO> entityDefinitionDTO = createEntityDefinitionDTO(world);
            return new RunStatisticsDTO(entityDefinitionDTO);
        }
        throw new ErrorException("No such simulation");
    }

    private List<StatisticEntityDTO> createEntityDefinitionDTO(World world) {
        // wait for world to initialize
        waitForWorldToInitialize(world);

        // create the entity DTOs
        List<StatisticEntityDTO> entityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : world.getEntityDefinitionMap().values()) {
            List<Integer> populationOverTime = world.getPopulationOverTime(entityDefinition.getName());

            List<StatisticPropertyDTO> propertyDTOList = new ArrayList<>();
            for (Property property : entityDefinition.getProperties().values()) {
                List<Entity> entityList = world.getEntities().get(entityDefinition.getName());
                StatisticPropertyDTO statisticPropertyDTO = createPropertyDTO(property, entityList);
                propertyDTOList.add(statisticPropertyDTO);
            }
            entityDTOList.add(new StatisticEntityDTO(propertyDTOList, entityDefinition.getName(), entityDefinition.getPopulation(), populationOverTime));
        }
        return entityDTOList;
    }

    private StatisticPropertyDTO createPropertyDTO(Property property, List<Entity> entityList) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        Float avgSum = 0f;
        String avg="";
        Float consistencySum = 0f;

        for (Entity entity : entityList) {
            Property prop = entity.getPropertyByName(property.getName());
            if (prop.getType() == PropertyType.FLOAT)
                avgSum+= (Float) prop.getValue();
            consistencySum += prop.getPropertyConsistency();
            if (frequencyMap.containsKey(prop.getValue().toString())) {
                Integer value = frequencyMap.get(prop.getValue().toString());

                frequencyMap.put(prop.getValue().toString(), value + 1);
            } else
                frequencyMap.put(prop.getValue().toString(), 1);
        }

        if (property.getType() == PropertyType.FLOAT)
            avgSum /= entityList.size();
        consistencySum /= entityList.size();

       avg = (property.getType() != PropertyType.FLOAT) ? "N/A" : avgSum.toString();

        return new StatisticPropertyDTO(property.getName(), property.getType().propertyClass.getSimpleName(), frequencyMap, consistencySum, avg);

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
            //this.pastSimulationArtifactDTO = new ArrayList<>();

            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.template = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            try {
                int threadAmount = this.template.getPRDThreadCount();
                this.executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadAmount);
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
    private void waitForWorldToInitialize(World world){
        while(!world.isInitialized())//todo is this ok?
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public List<RunEndDTO> getPastArtifacts() {
        List<RunEndDTO> pastSimulationArtifactDTO = new ArrayList<>();
        for (Map.Entry entry : worlds.entrySet()) {
            World world = (World) entry.getValue();
            String UUID = (String) entry.getKey();
            boolean exists = false;
            if(world.isHasThreadStarted()) {
                    waitForWorldToInitialize(world);
                    RunEndDTO newRunEndDTO = world.getRunEndDTO(UUID);
                    pastSimulationArtifactDTO.add(newRunEndDTO);
            }

        }
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
            //this.pastSimulationArtifactDTO = engine.pastSimulationArtifactDTO;
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

    public Integer getCurrentTickByUUID(String runUUID)  {
        return worlds.get(runUUID).getTicks();
    }
    public boolean isSimulationRunning(String runUUID) {
        return worlds.get(runUUID).isRunning();
    }

    public Integer getMaxTickByUUID(String uuid) {
        return worlds.get(uuid).getTerminationByTicks();
    }

    public Integer getCurrentSecondByUUID(String uuid) {
        return worlds.get(uuid).getSeconds();
    }

    public Integer getMaxSecondsByUUID(String uuid) {
        return worlds.get(uuid).getTerminationBySeconds();
    }

    public void shutdownExecutorService() {
        if(executorService!=null) {
            executorService.getQueue().clear();
            executorService.shutdownNow();
        }
    }

    public String getThreadPoolSize() {
        return String.valueOf(this.executorService.getMaximumPoolSize());
    }
}

