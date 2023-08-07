package engine.entity;

import engine.factory.EntityFactory;
import engine.world.utils.Property;

import java.util.List;

public class EntityDefinition {
    private final String name;
    private final int population;
    private final List<Property> entityProperties;
    public EntityDefinition(String name, List<Property> entityProperties, int population) {
        this.name = name;
        this.entityProperties = entityProperties;
        this.population = population;
    }
    public String getName() {
        return name;
    }
    public Property getPropertyByName(String name) {
        for (Property property : entityProperties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return "EntityDefinition{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", entityProperties=" + entityProperties +
                '}';
    }
    public List<Property> getEntityProperties() {
        return entityProperties;
    }

    public List<Entity> createEntityList() {
        return EntityFactory.createEntityList(this);
    }

    public int getPopulation() {
        return population;
    }

    public List<Property> getProperties() {
        return entityProperties;
    }
}