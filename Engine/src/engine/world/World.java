package engine.world;

import DTO.RunEndDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.factory.EntityFactory;
import engine.factory.PropertyFactory;
import engine.factory.RuleFactory;
import engine.jaxb.schema.generated.PRDBySecond;
import engine.jaxb.schema.generated.PRDByTicks;
import engine.jaxb.schema.generated.PRDWorld;
import engine.rule.Rule;
import engine.world.utils.Property;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World implements java.io.Serializable {
    private final List<Rule> rules;
    private List<Property> environmentVariables;//todo change this to map
    private Map<String, List<Entity>> entityList;
    private Map<String, EntityDefinition> entityDefinitionMap;
    private Integer terminationByTicks;
    private Integer terminationBySeconds;

    public World(PRDWorld prdWorld) throws ErrorException {
        try {
            Map<String, EntityDefinition> entityDefinitionList = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
            this.setEntityDefinitionMap(entityDefinitionList);
            List<Property> environmentVariables = PropertyFactory.createPropertyList(prdWorld.getPRDEvironment().getPRDEnvProperty());
            this.setEnvironmentVariables(environmentVariables);
            List<Rule> rules = RuleFactory.createRuleList(this, prdWorld.getPRDRules().getPRDRule());
            List<Object> termination = prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond();

            for (Object object : termination) {
                if (object instanceof PRDByTicks) {
                    terminationByTicks = ((PRDByTicks) object).getCount();
                } else if (object instanceof PRDBySecond) {
                    terminationBySeconds = ((PRDBySecond) object).getCount();
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

    public RunEndDTO run() throws ErrorException, WarnException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(formatter);

        long startTime = System.currentTimeMillis();
        Integer ticks = 0;
        createEntities();
        String finishedBy = checkTerminationConditions(ticks, startTime);
        while (finishedBy.isEmpty()) {
            for (List<Entity> entities : entityList.values()) {
                for (Entity entity : entities) {
                    for (Rule rule : rules) {
                        rule.applyRule(this, entity, ticks);
                    }
                }
            }
            RemoveEntities();
            ticks++;
            finishedBy = checkTerminationConditions(ticks, startTime);
        }
        String UUID = java.util.UUID.randomUUID().toString();
        return new RunEndDTO(UUID, finishedBy, formattedDateTime);
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
}
