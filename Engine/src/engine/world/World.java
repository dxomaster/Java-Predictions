package engine.world;

import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.Rule;
import engine.world.utils.Property;

import java.util.List;
import java.util.Map;

public class World {
    private static List<Property> environmentVariables;
    private static Map<String, List<Entity>> entityList;
    private static List<EntityDefinition> entityDefinitionList;
    private final List<Rule> rules;
    private final Integer terminationByTicks;
    private final Integer terminationBySeconds;

    public World(List<Property> environmentVariables, List<Rule> rules, Integer terminationByTicks, Integer terminationBySeconds, List<EntityDefinition> entityDefinitionList) {
        if (terminationByTicks == null && terminationBySeconds == null)
            throw new IllegalArgumentException("At least one termination condition must be specified");
        World.entityDefinitionList = entityDefinitionList;
        World.environmentVariables = environmentVariables;
        this.rules = rules;
        if (terminationByTicks != null && terminationByTicks < 1)
            throw new IllegalArgumentException("Termination by ticks must be greater than 0");
        if (terminationBySeconds != null && terminationBySeconds < 1)
            throw new IllegalArgumentException("Termination by seconds must be greater than 0");

        this.terminationByTicks = terminationByTicks;
        this.terminationBySeconds = terminationBySeconds;
    }

    public static void setEnvironmentVariables(List<Property> environmentVariables) {
        World.environmentVariables = environmentVariables;
    }

    public static void setEntityDefinitionList(List<EntityDefinition> entityDefinitionList) {
        World.entityDefinitionList = entityDefinitionList;
    }

    public static Property getEnvironmentVariableByName(String name) {
        for (Property property : environmentVariables) {
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
        out.append("Entity Definitions:\n");
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            out.append(entityDefinition.toString()).append("\n");
        }
        out.append("Rules:\n");
        for (Rule rule : rules) {
            out.append(rule.toString()).append("\n");

        }
        return out.toString();

    }

    public static void RemoveEntity(Entity toRemove) {
        List<Entity> listToSearchIn = entityList.get(toRemove.getName());
        //listToSearchIn.removeIf(entity -> entity.equals(toRemove));


    }

    public void createEntities() {
        entityList = new java.util.HashMap<>();
        if (entityDefinitionList == null)
            throw new IllegalArgumentException("Entity definition list is empty");
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            List<Entity> entityList = entityDefinition.createEntityList();
            World.entityList.put(entityDefinition.getName(), entityList);
        }
    }

    public static EntityDefinition getEntityDefinitionByName(String name) {
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            if (entityDefinition.getName().equals(name)) {
                return entityDefinition;
            }
        }
        return null;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        Integer ticks = 0;
        createEntities();
        while (!checkTerminationConditions(ticks, startTime)) {
            for (List<Entity> entities : entityList.values()) {
                for (Entity entity : entities) {
                    for (Rule rule : rules) {
                        try {
                            rule.applyRule(entity, ticks);
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
