package engine.world;

import engine.entity.Entity;
import engine.rule.Rule;
import engine.world.utils.Property;

import java.util.List;

public class World {
    private final List<Property<?>> environmentVariables;
    private final List<List<Entity>> entityList;
    private final List<Rule> rules;
    private final Integer terminationByTicks;
    private final Integer terminationBySeconds;
    public World(List<Property<?>> environmentVariables, List<List<Entity>> entityList, List<Rule> rules, Integer terminationByTicks, Integer terminationBySeconds) {
        if (terminationByTicks == null && terminationBySeconds == null)
            throw new IllegalArgumentException("At least one termination condition must be specified");
        this.environmentVariables = environmentVariables;
        this.entityList = entityList;
        this.rules = rules;
        if(terminationByTicks!=null && terminationByTicks<1)
            throw new IllegalArgumentException("Termination by ticks must be greater than 0");
        if (terminationBySeconds!=null && terminationBySeconds<1)
            throw new IllegalArgumentException("Termination by seconds must be greater than 0");

        this.terminationByTicks = terminationByTicks;
        this.terminationBySeconds = terminationBySeconds;
    }

    public List<Property<?>> getEnvironmentVariables() {
        return environmentVariables;
    }

    public List<List<Entity>> getEntityList() {
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
        return "World{"+
                "entityList=" + entityList +
                ", rules=" + rules +
                ", terminationByTicks=" + terminationByTicks +
                ", terminationBySeconds=" + terminationBySeconds +
                '}';
    }

    public Integer getTerminationBySeconds() {
        return terminationBySeconds;
    }
}
