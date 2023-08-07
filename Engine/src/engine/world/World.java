package engine.world;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.Rule;
import engine.world.utils.Property;

import java.util.List;
import java.util.Map;

public class World {
    private static List<Property> environmentVariables;
    private static Map<String,List<Entity>> entityList;
    private static List<EntityDefinition> entityDefinitionList;
    private final List<Rule> rules;
    private final Integer terminationByTicks;
    private final Integer terminationBySeconds;
    public World(List<Property> environmentVariables, List<Rule> rules, Integer terminationByTicks, Integer terminationBySeconds,List<EntityDefinition> entityDefinitionList) {
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
    public World(List<Property> environmentVariables, Map<String,List<Entity>> entityList, List<Rule> rules, Integer terminationByTicks, Integer terminationBySeconds, List<EntityDefinition> entityDefinitionList) {

        if (terminationByTicks == null && terminationBySeconds == null)
            throw new IllegalArgumentException("At least one termination condition must be specified");
        World.entityDefinitionList = entityDefinitionList;
        World.environmentVariables = environmentVariables;
        World.entityList = entityList;
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
    public List<Property> getEnvironmentVariables() {
        return environmentVariables;
    }

    public Map<String,List<Entity>> getEntityList() {
        return entityList;
    }

    public static void setEntityList(Map<String, List<Entity>> entityList) {
        World.entityList = entityList;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Integer getTerminationByTicks() {
        return terminationByTicks;
    }

    @Override
    public String toString() {
        return "World{" +
                "entityDefinitions=" + entityDefinitionList +
                ", rules=" + rules +
                ".environmentVariables=" + environmentVariables +
                ", terminationByTicks=" + terminationByTicks +
                ", terminationBySeconds=" + terminationBySeconds +
                '}';
    }
    public static void RemoveEntity(Entity toRemove)
    {
        List<Entity> listToSearchIn = entityList.get(toRemove.getName());
        for (Entity entity : listToSearchIn){
            if (entity == toRemove) {
                listToSearchIn.remove(entity);
                return;
            }

        }
    }
    public void createEntities()
    {
        entityList = new java.util.HashMap<>();
        if (entityDefinitionList == null)
            throw new IllegalArgumentException("Entity definition list is empty");
        for (EntityDefinition entityDefinition : entityDefinitionList) {
            List<Entity> entityList = entityDefinition.createEntityList();
            World.entityList.put(entityDefinition.getName(),entityList);
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
    public Integer getTerminationBySeconds() {
        return terminationBySeconds;
    }
}
