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
    private final List<EntityDefinition> entityDefinitionList;
    private final List<Rule> rules;
    private final Integer terminationByTicks;
    private final Integer terminationBySeconds;

    public World(List<Property> environmentVariables, Map<String,List<Entity>> entityList, List<Rule> rules, Integer terminationByTicks, Integer terminationBySeconds, List<EntityDefinition> entityDefinitionList) {

        if (terminationByTicks == null && terminationBySeconds == null)
            throw new IllegalArgumentException("At least one termination condition must be specified");
        this.environmentVariables = environmentVariables;
        this.entityList = entityList;
        this.rules = rules;
        if (terminationByTicks != null && terminationByTicks < 1)
            throw new IllegalArgumentException("Termination by ticks must be greater than 0");
        if (terminationBySeconds != null && terminationBySeconds < 1)
            throw new IllegalArgumentException("Termination by seconds must be greater than 0");

        this.terminationByTicks = terminationByTicks;
        this.terminationBySeconds = terminationBySeconds;
        this.entityDefinitionList = entityDefinitionList;
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
    public Integer getTerminationBySeconds() {
        return terminationBySeconds;
    }
}
