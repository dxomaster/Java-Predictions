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
import rule.Rule;
import world.utils.Grid;
import world.utils.Property;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class World implements java.io.Serializable, Runnable {
    private  String formattedDateTime;
    private String finishedReason;
    private final List<Rule> rules;
    private List<Property> environmentVariables;//todo change this to map
    private Map<String, List<Entity>> entityList;
    private Map<String, EntityDefinition> entityDefinitionMap;
    private Integer ticks = 0;
    private Integer terminationByTicks;
    private Integer terminationBySeconds;
    private final int row;
    private final int column;

    private Grid grid;

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

        this.terminationByTicks = world.terminationByTicks;
        this.terminationBySeconds = world.terminationBySeconds;
        this.row = world.row;
        this.column = world.column;
    }

    public World(PRDWorld prdWorld) throws ErrorException {
        try {
            Map<String, EntityDefinition> entityDefinitionList = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
            this.setEntityDefinitionMap(entityDefinitionList);
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
            if (row < 1 || column < 1)
                throw new IllegalArgumentException("Grid size must be greater than 0");
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
                    entityDefinitionMap.get(entity.getName()).setFinalPopulation(entityDefinitionMap.get(entity.getName()).getFinalPopulation() - 1);
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

    public void run() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.formattedDateTime = currentDateTime.format(formatter);
        long startTime = System.currentTimeMillis();

        try {
            createEntities();
            grid = new Grid(row, column, entityList);
            String finishedReason = checkTerminationConditions(ticks, startTime);
            while (finishedReason.isEmpty()) {
                for (List<Entity> entities : entityList.values()) {
                    for (Entity entity : entities) {

                        this.grid.moveEntity(entity);
                    }
                }

                for (List<Entity> entities : entityList.values()) {

                    for (Entity entity : entities) {
                        for (Rule rule : rules) {
                            rule.applyRule(this, entity, ticks);
                        }
                    }
                }
                RemoveEntities();
                ticks++;
                this.finishedReason = checkTerminationConditions(ticks, startTime);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String checkTerminationConditions(Integer ticks, long startTime) {
        String finishedBy = "";
        if (terminationByTicks != null && ticks >= terminationByTicks)
            finishedBy += "ticks\n";
        if (terminationBySeconds != null && (System.currentTimeMillis() - startTime) / 1000 >= terminationBySeconds)
            finishedBy += " seconds\n";
        return finishedBy;
    }

    public List<Property> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(List<Property> environmentVariables) {
        this.environmentVariables = environmentVariables;
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
        entityDefinitionMap.get(entityToCreate).setPopulation(entityDefinitionMap.get(entityToCreate).getPopulation() + 1);
        Entity newEntity = EntityFactory.createEntity(entityDefinitionMap.get(entityToCreate));
        entityList.get(entityToCreate).add(newEntity);
        newEntity.setLocation(grid.getRandomUnoccupiedLocation());

    }

    public void createEntityDerived(String entityToCreate, Entity entity) throws WarnException {

        entityDefinitionMap.get(entityToCreate).setPopulation(entityDefinitionMap.get(entityToCreate).getPopulation() + 1);
        Entity newEntity = EntityFactory.createEntityDerived(entityDefinitionMap.get(entityToCreate), entity);
        newEntity.setLocation(entity.getLocation());
        entityList.get(entityToCreate).add(newEntity);

    }

    public Integer getTicks() {
        return this.ticks;
    }

    public List<Entity> getRandomEntities(String name, int count) {
        List<Entity> entities = new ArrayList<>();
        List<Entity> entityList = this.entityList.get(name);
        for (int i = 0; i < count; i++) {
            entities.add(entityList.get(new Random().nextInt(entityList.size())));
        }
        return entities;
    }
}
