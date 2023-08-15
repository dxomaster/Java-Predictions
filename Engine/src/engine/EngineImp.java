package engine;

import DTO.*;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDWorld;
import engine.world.World;
import engine.world.utils.Property;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineImp implements engine.Engine, Serializable {
    String simulationName;
    private List<RunEndDTO> pastSimulationArtifactDTO = new ArrayList<>();
    private Map<String, World> pastSimulationWorlds = new HashMap<>();
    private World world;

    public RunEndDTO runSimulation() throws ErrorException {

        RunEndDTO finishedArtifact = world.run();

        pastSimulationWorlds.put(finishedArtifact.getUUID(), world);
        world = null;

        try {
            File file = new File(this.simulationName);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PRDWorld jaxbWorld = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            this.world = new World(jaxbWorld);
            this.simulationName = file.getName();

        } catch (JAXBException e) {
            throw new ErrorException("Error in loading file please make sure this is a valid XML file with the correct schema.");
        }

        this.pastSimulationArtifactDTO.add(finishedArtifact);
        //this.pastSimulationArtifactDTO.put(finishedArtifact.getUUID(),pastSimulationArtifactDTO);
        return finishedArtifact;
    }

    public boolean isSimulationLoaded() {
        return world != null;
    }

//    private PropertyDTO createPropertyDTO(Property property) {
//        return new PropertyDTO(property.getName(), property.getType().propertyClass.getSimpleName(), property.getValue().toString());
//    }

//    private EntityDTO createEntityDTO(Entity entity) {
//        List<PropertyDTO> propertyDTOList = new ArrayList<>();
//        for (Property property : entity.getProperties()) {
//            propertyDTOList.add(createPropertyDTO(property));
//        }
//        return new EntityDTO(propertyDTOList, entity.getName()
//                , world.getEntityDefinitionByName(entity.getName()).getPopulation());
//    }

    public RunStatisticsDTO getPastSimulationArtifactDTO(String uuid) throws ErrorException {
        if (pastSimulationWorlds.containsKey(uuid)) {
            World world = pastSimulationWorlds.get(uuid);
            //Map<String, List<EntityDTO>> entitiesDTO = createWorldEntitiesDTO(world.getEntities());
            List<EntityDTO> entityDefinitionDTO = createEntityDefinitionDTO(world);
            return new RunStatisticsDTO(world.getFormattedDate(), uuid, entityDefinitionDTO);
        }
        throw new ErrorException("No such simulation");
    }

    private List<EntityDTO> createEntityDefinitionDTO(World world) {
        List<EntityDTO> entityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : world.getEntityDefinitionMap().values()) {
            List<PropertyDTO> propertyDTOList = new ArrayList<>();
            for (Property property : entityDefinition.getProperties().values()) {

                List<Entity> entityList = world.getEntities().get(entityDefinition.getName());
                PropertyDTO propertyDTO= createPropertyDTO(property,entityList);
                propertyDTOList.add(propertyDTO);
            }
            entityDTOList.add(new EntityDTO(propertyDTOList, entityDefinition.getName(), entityDefinition.getPopulation(),entityDefinition.getFinalPopulation()));
        }
        return entityDTOList;
    }
    private PropertyDTO createPropertyDTO(Property property,List<Entity> entityList){
        Map<String,Integer> frequencyMap = new HashMap<>();
        for(Entity entity : entityList){
            Property prop = entity.getPropertyByName(property.getName());
            if(frequencyMap.containsKey(prop.getValue().toString())){
                Integer value = frequencyMap.get(prop.getValue().toString());

                frequencyMap.put(prop.getValue().toString(),value +1);
            }
            else
                frequencyMap.put(prop.getValue().toString(), 1);
        }
        return new PropertyDTO(property.getName(),property.getType().propertyClass.getSimpleName(),frequencyMap);

    }


//    private Map<String, List<EntityDTO>> createWorldEntitiesDTO(Map<String, List<Entity>> entities) {
//        Map<String, List<EntityDTO>> entitiesDTO = new HashMap<>();
//        for (String entityName : entities.keySet()) {
//            List<Entity> entityList = entities.get(entityName);
//            List<EntityDTO> entityDTOList = new ArrayList<>();
//            for (Entity entity : entityList) {
//                entityDTOList.add(createEntityDTO(entity));
//            }
//            entitiesDTO.put(entityName, entityDTOList);
//        }
//        return entitiesDTO;
//    }

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

    public String getSimulationName() {
        return simulationName;
    }

    public void loadSimulationParametersFromFile(String filename) throws ErrorException {
        try {
            this.pastSimulationArtifactDTO = new ArrayList<>();

            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PRDWorld jaxbWorld = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            this.world = new World(jaxbWorld);
            this.pastSimulationWorlds = new HashMap<>();
            this.simulationName = file.getName();

        } catch (JAXBException e) {
            throw new ErrorException("Error in loading file please make sure this is a valid XML file with the correct schema.");
        }


    }

    public WorldPrintDTO getSimulationParameters() {
        if (world == null)
            throw new IllegalArgumentException("No file is loaded");
        return new WorldPrintDTO(world.toString());//todo change to WorldDTO
    }

    public List<RunEndDTO> getPastArtifacts() {
        return pastSimulationArtifactDTO;
    }

    public void saveEngineToFile(String filename) throws ErrorException {
        try (ObjectOutputStream out =
                    new ObjectOutputStream(
                            Files.newOutputStream(Paths.get(filename)))){
                out.writeObject(this);
                out.flush();

        }
        catch (Exception e){
            throw new ErrorException("Error in saving file.");
        }
    }

    public void loadEngineFromFile(String filenameToLoad) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(filenameToLoad)))) {
            // we know that we read array list of Persons
            EngineImp engineImp = (EngineImp) in.readObject();
            this.pastSimulationArtifactDTO = engineImp.pastSimulationArtifactDTO;
            this.pastSimulationWorlds = engineImp.pastSimulationWorlds;
            this.simulationName = engineImp.simulationName;
            this.world = engineImp.world;


        }
    }
}

