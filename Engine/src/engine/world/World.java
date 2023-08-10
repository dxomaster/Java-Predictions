package engine.world;

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
    private List<Property> environmentVariables;
    private Map<String, List<Entity>> entityList;
    private List<EntityDefinition> entityDefinitionList;
    private List<Rule> rules;
    private Integer terminationByTicks;
    private Integer terminationBySeconds;
    private String formattedDateTime;

    public World(PRDWorld prdWorld) {
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
            System.out.println(e.getMessage());
        }
    }

    public void setEnvironmentVariables(List<Property> environmentVariables) {
        this.environmentVariables = environmentVariables;
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
        for (List<Entity> currentList: entityList.values()) {
            List<Entity> mockList = new ArrayList<Entity>(currentList);
            for (Entity enitity: mockList) {
                if (enitity.getToKill())
                    currentList.remove(enitity);
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

    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.formattedDateTime = currentDateTime.format(formatter);

        long startTime = System.currentTimeMillis();
        Integer ticks = 0;
        createEntities();
        while (!checkTerminationConditions(ticks, startTime)) {
            for (List<Entity> entities : entityList.values()) {
                for (Entity entity : entities) {
                    for (Rule rule : rules) {
                        try {
                            rule.applyRule(this, entity, ticks);
                        } catch (WarnException ignored) {

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            ticks++;
        }
        System.out.println("done");

    }


    private boolean checkTerminationConditions(Integer ticks, long startTime) {
        if (terminationByTicks != null && ticks >= terminationByTicks)
            return true;
        return terminationBySeconds != null && (System.currentTimeMillis() - startTime) / 1000 >= terminationBySeconds;
    }
}
