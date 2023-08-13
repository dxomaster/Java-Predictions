package engine.world;

import DTO.SimulationArtifactDTO;
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

public class World {
    private List<Property> environmentVariables;//todo change this to map
    private Map<String, List<Entity>> entityList;
    private List<EntityDefinition> entityDefinitionList;//todo change this to map
    private final List<Rule> rules;
    private Integer terminationByTicks;
    private Integer terminationBySeconds;
    private String UUID;
    private String formattedDateTime;

    public World(PRDWorld prdWorld) throws ErrorException {
        try {
            List<EntityDefinition> entityDefinitionList = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
            this.setEntityDefinitionList(entityDefinitionList);
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
            this.entityDefinitionList = entityDefinitionList;
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

    public void setEntityDefinitionList(List<EntityDefinition> entityDefinitionList) {
        this.entityDefinitionList = entityDefinitionList;
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
        out.append("World:\n" +
                "Environment Variables:\n");
        for (Property environmentVariable : environmentVariables) {
            out.append(environmentVariable.toString()).append("\n");

        }
        out.append("\nEntity Definitions:\n");
        for (EntityDefinition entityDefinition : entityDefinitionList) {
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
                if (entity.getToKill())
                    currentList.remove(entity);
            }
        }
    }

    public void createEntities() {
        entityList = new java.util.HashMap<>();
        if (entityDefinitionList == null)
            throw new IllegalArgumentException("Entity definition list is empty");
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            List<Entity> entityList = entityDefinition.createEntityList();
            this.entityList.put(entityDefinition.getName(), entityList);
        }
    }

    public EntityDefinition getEntityDefinitionByName(String name) {
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            if (entityDefinition.getName().equals(name)) {
                return entityDefinition;
            }
        }
        return null;
    }

    public SimulationArtifactDTO run() throws ErrorException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.formattedDateTime = currentDateTime.format(formatter);

        long startTime = System.currentTimeMillis();
        Integer ticks = 0;
        createEntities();
        String finishedBy = checkTerminationConditions(ticks, startTime);
        while (finishedBy.equals("")) {
            for (List<Entity> entities : entityList.values()) {
                for (Entity entity : entities) {
                    for (Rule rule : rules) {
                        try {
                            rule.applyRule(this, entity, ticks);
                        } catch (WarnException ignored) {
                            //these exceptions are OK, continue to next rule
                        }

                    }
                }
            }
            RemoveEntities();
            ticks++;
            finishedBy = checkTerminationConditions(ticks, startTime);
        }
        this.UUID = java.util.UUID.randomUUID().toString();
        return new SimulationArtifactDTO(this.UUID, finishedBy);
    }

    public String getUUID() {
        return UUID;
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

    public void setEnvironmentVariable(String name, Object value) throws WarnException {
        for (Property property : environmentVariables) {
            if (property.getName().equals(name)) {
                property.setValue(value);
                return;
            }
        }
        throw new IllegalArgumentException("Environment variable " + name + " does not exist");
    }
}
