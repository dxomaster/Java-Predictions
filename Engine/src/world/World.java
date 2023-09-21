package world;

import DTO.*;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import entity.EntityDefinition;
import factory.EntityFactory;
import factory.PropertyFactory;
import factory.RuleFactory;
import engine.jaxb.schema.generated.PRDBySecond;
import engine.jaxb.schema.generated.PRDByTicks;
import engine.jaxb.schema.generated.PRDWorld;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import rule.Rule;
import world.utils.Grid;
import world.utils.Property;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class World implements java.io.Serializable, Runnable {
    public boolean isHasThreadStarted() {
        return hasThreadStarted;
    }

    private boolean hasThreadStarted = false;
    private  String formattedDateTime;
    private boolean isRunning = false;

    public boolean isPaused() {
        return isPaused;
    }

    public synchronized void setStopped(boolean stopped) {
        isStopped = stopped;
        notifyAll();
    }

    private boolean isStopped = false;

    private boolean isPaused = false;

    private String finishedReason = "Did not finish";
    private final List<Rule> rules;
    private List<Property> environmentVariables;//todo change this to map
    private Map<String, List<Entity>> entityList;
    private Map<String, EntityDefinition> entityDefinitionMap;
    private final List<Entity> creationBuffer = new ArrayList<>();
    private final Map<String, List<Integer>> entityPopulationOverTime;
    private Integer ticks = 0;

    public boolean isInitialized() {
        return isInitialized;
    }

    private boolean isInitialized = false;
    private Integer terminationByTicks;
    private Integer terminationBySeconds;
    private final int row;
    private final int column;
    private Grid grid;
    private long startTime;
    private long currentTime;

    public String getErrorMessage() {
        return errorMessage;
    }

    private String errorMessage = "";

    public Grid getGrid() {
        return grid;
    }

    public WorldDTO getWorldDTO() {
        List<PropertyDTO> environmentVariables = new ArrayList<>();
        for (Property property : this.environmentVariables) {
            environmentVariables.add(property.getPropertyDTO());
        }
        List<EntityDTO> entityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : entityDefinitionMap.values()) {
            entityDTOList.add(entityDefinition.getEntityDTO());
        }
        List<RuleDTO> ruleDTOList = new ArrayList<>();
        for (Rule rule : rules) {
            ruleDTOList.add(rule.getRuleDTO());
        }
        TerminationDTO termination = new TerminationDTO(this.terminationByTicks, this.terminationBySeconds);

        return new WorldDTO(environmentVariables, entityDTOList, ruleDTOList, termination);
    }

    public World(World world) {
        entityDefinitionMap = new java.util.HashMap<>();
        for (EntityDefinition entityDefinition : world.entityDefinitionMap.values()) {
            this.entityDefinitionMap.put(entityDefinition.getName(), new EntityDefinition(entityDefinition));
        }
        this.environmentVariables = new ArrayList<>();
        for (Property property : world.environmentVariables) {
            this.environmentVariables.add(new Property(property));
        }
        this.rules = new ArrayList<>();
        for (Rule rule : world.rules) {
            this.rules.add(new Rule(rule));
        }

        this.entityPopulationOverTime = new HashMap<>();
        for (String entityName : world.entityPopulationOverTime.keySet()) {
            this.entityPopulationOverTime.put(entityName, new ArrayList<>(world.entityPopulationOverTime.get(entityName)));
        }

        this.terminationByTicks = world.terminationByTicks;
        this.terminationBySeconds = world.terminationBySeconds;
        this.row = world.row;
        this.column = world.column;
    }

    public World(PRDWorld prdWorld) throws ErrorException {
        try {
            Map<String, EntityDefinition> entityDefinitionList = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
            this.setEntityDefinitionMap(entityDefinitionList);
            entityPopulationOverTime = new HashMap<>();
            for (EntityDefinition entityDefinition : entityDefinitionMap.values()) {
                entityPopulationOverTime.put(entityDefinition.getName(), new ArrayList<>());
            }
            List<Property> environmentVariables = PropertyFactory.createPropertyList(prdWorld.getPRDEnvironment().getPRDEnvProperty());
            this.setEnvironmentVariables(environmentVariables);
            List<Rule> rules = RuleFactory.createRuleList(this, prdWorld.getPRDRules().getPRDRule());
            List<Object> termination = prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks();

            for (Object object : termination) {
                if (object instanceof PRDByTicks) {
                    this.terminationByTicks = ((PRDByTicks) object).getCount();
                } else if (object instanceof PRDBySecond) {
                    this.terminationBySeconds = ((PRDBySecond) object).getCount();
                }
            }
            if (terminationByTicks == null && terminationBySeconds == null)
                throw new IllegalArgumentException("At least one termination condition must be specified");
            this.entityDefinitionMap = entityDefinitionList;
            this.environmentVariables = environmentVariables;
            this.rules = rules;
            if (terminationByTicks != null && terminationByTicks < 1)
                throw new IllegalArgumentException("Termination by ticks must be greater than 0");
            if (terminationBySeconds != null && terminationBySeconds < 1)
                throw new IllegalArgumentException("Termination by seconds must be greater than 0");
            row = prdWorld.getPRDGrid().getRows();
            column = prdWorld.getPRDGrid().getColumns();
            if (row < 10 || column < 10 || row > 100 || column > 100)
                throw new IllegalArgumentException("Grid size must be between 10 and 100");
        } catch (Exception e) {
            throw new ErrorException("Error in creating world: " + e.getMessage());
        }
    }

    public Property getEnvironmentVariableByName(String name) {
        for (Property property : this.environmentVariables) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\nEntities:\n");
        for (EntityDefinition entityDefinition : entityDefinitionMap.values()) {
            out.append(entityDefinition.toString()).append("\n");
        }
        out.append("\nRules:\n");
        for (Rule rule : rules) {
            out.append(rule.toString()).append("\n");

        }
        return out.toString();

    }

    public void RemoveEntities() {
        for (List<Entity> currentList : entityList.values()) {
            List<Entity> mockList = new ArrayList<>(currentList);
            for (Entity entity : mockList) {
                if (entity.getToKill()) {
                    grid.removeEntity(entity);
                    currentList.remove(entity);
                }

            }
        }
    }

    public void createEntities() throws WarnException {
        entityList = new java.util.HashMap<>();
        if (entityDefinitionMap == null)
            throw new IllegalArgumentException("Entity definition list is empty");
        for (EntityDefinition entityDefinition : entityDefinitionMap.values()) {
            List<Entity> entityList = entityDefinition.createEntityList();
            this.entityList.put(entityDefinition.getName(), entityList);
        }
    }

    public EntityDefinition getEntityDefinitionByName(String name) {
        return entityDefinitionMap.get(name);
    }
    public RunEndDTO getRunEndDTO(String UUID)
    {
        String finishedReason = this.finishedReason;
        String status;
        if(this.getErrorMessage().isEmpty()) {
            if (this.isRunning())
                status = "Running";
            else if (this.isPaused()) {
                status = "Paused";
            }
            else {
                status = "Completed";
            }
        }
        else {
            status = "Completed";
        }
        if(this.finishedReason.isEmpty())
            finishedReason = "Did not finish";
        return new RunEndDTO(UUID, finishedReason, this.formattedDateTime, this.errorMessage, status);
    }
    public void run() {
        hasThreadStarted = true;
        this.isRunning = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.formattedDateTime = currentDateTime.format(formatter);
        startTime = System.currentTimeMillis();

        try {
            createEntities();
            grid = new Grid(row, column, entityList);
            checkTerminationConditions(ticks.intValue());
            this.isInitialized = true;
            while (finishedReason.isEmpty()) {


                moveEntities();

                List<Rule> activatedRules = getActivatedRules();

                for (List<Entity> entities : entityList.values()) {
                    for (Entity entity : entities) {
                        for (Rule rule : activatedRules) {
                            rule.applyRule(this, entity, ticks.intValue());
                        }
                    }
                }

                RemoveEntities();

                createReplacedEntities();

                updatePopulationOverTime();

                ticks++;
                currentTime = System.currentTimeMillis();
                //Thread.sleep(1000);
                checkTerminationConditions(ticks.intValue());
                // todo notify user when simulation is finished
                synchronized (this) {
                    while (isPaused) {
                        wait();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(this.ticks);
            e.printStackTrace();
            this.errorMessage = e.getMessage();
        }
        this.isRunning = false;
        System.out.println(this);
        System.out.println("Finished by: " + finishedReason);
        System.out.println(Thread.currentThread().getName());
    }
    private List<Rule> getActivatedRules()
    {
        List<Rule> activatedRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.getActivation().isActivated(ticks.intValue()))
                activatedRules.add(rule);
        }
        return activatedRules;
    }
    private void moveEntities()
    {
        for (List<Entity> entities : entityList.values()) {
            for (Entity entity : entities) {

                this.grid.moveEntity(entity);
            }
        }
    }
    private void updatePopulationOverTime() {
        for (String entityType : entityPopulationOverTime.keySet()) {
            int population = entityList.get(entityType).size();
            entityPopulationOverTime.get(entityType).add(population);
            entityDefinitionMap.get(entityType).setCurrentPopulation(population);//todo is this ok?
        }
    }
    private void checkTerminationConditions(Integer ticks) {
        this.finishedReason = "";

        if (terminationByTicks != null && ticks >= terminationByTicks)
            this.finishedReason += "ticks\n";
        if (terminationBySeconds != null && (System.currentTimeMillis() - startTime) / 1000 >= terminationBySeconds)
            this.finishedReason += " seconds\n";
        if (isStopped)
            this.finishedReason += "user\n";
    }

    public List<Property> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(List<Property> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }
    public void createReplacedEntities()
    {
        for(Entity entity : creationBuffer)
        {
            if (grid.getOccupiedSize() + 1 <= grid.getTotalSize()) {
                entity.setLocation(grid.getRandomUnoccupiedLocation());
                grid.addEntity(entity);
                entityList.get(entity.getName()).add(entity);
            } //todo ask aviad
        }
        creationBuffer.clear();
    }
    public Map<String, List<Entity>> getEntities() {
        return entityList;
    }

    public Map<String, EntityDefinition> getEntityDefinitionMap() {
        return entityDefinitionMap;
    }

    public void setEntityDefinitionMap(Map<String, EntityDefinition> entityDefinitionMap) {
        this.entityDefinitionMap = entityDefinitionMap;
    }

    public void updateEntityPopulation(String name, Integer newValue) {
        entityDefinitionMap.get(name).setPopulation(newValue);
    }

    public void createEntityFromScratch(String entityToCreate) throws WarnException {

        Entity newEntity = EntityFactory.createEntity(entityDefinitionMap.get(entityToCreate));
        creationBuffer.add(newEntity);
        //newEntity.setLocation(grid.getRandomUnoccupiedLocation()); not good here

    }

    public void createEntityDerived(String entityToCreate, Entity entity) throws WarnException {
        Entity newEntity = EntityFactory.createEntityDerived(entityDefinitionMap.get(entityToCreate), entity);
        newEntity.setLocation(entity.getLocation());
        creationBuffer.add(newEntity);

    }

    public Integer getTicks() {
        return this.ticks.intValue();
    }

    public List<Entity> getRandomEntities(String name, int count) {
        List<Entity> entities = new ArrayList<>();
        List<Entity> entityList = this.entityList.get(name);
        for (int i = 0; i < count; i++) {
            entities.add(entityList.get(new Random().nextInt(entityList.size())));
        }
        return entities;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getFinishedReason() {
        return finishedReason;
    }

    public String getStartingTime() {
        return formattedDateTime;
    }

    public List<Integer> getPopulationOverTime(String name) {
        return entityPopulationOverTime.get(name);
    }

    public synchronized void setPaused(boolean b) { this.isPaused = b;
    notifyAll();}

    public Integer getCurrentTickProperty() {
        return ticks;
    }

    public int getTerminationByTicks() {
        return terminationByTicks;
    }

    public Integer getSeconds() {
        return ((Long)((System.currentTimeMillis() - startTime) / 1000)).intValue();
    }

    public Integer getTerminationBySeconds() {
        return terminationBySeconds;
    }
}
