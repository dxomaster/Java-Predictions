package engine.entity;

import engine.world.utils.Property;

import java.util.List;

public class Entity {// todo: population
    private final String name;
    private final List<Property<?>> entityProperties;
    public Entity(String name, List<Property<?>> entityProperties) {
        this.name = name;
        this.entityProperties = entityProperties;
    }

    public List<Property<?>> getEntityProperties() {
        return entityProperties;
    }

    @Override
    public String toString() {
        return "Entity{" + entityProperties+"}";
    }

    public String getName() {
        return name;
    }
}
