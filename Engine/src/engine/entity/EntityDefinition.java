package engine.entity;

import engine.factory.EntityFactory;
import engine.world.utils.Property;

import java.util.List;
import java.util.Map;

public class EntityDefinition implements java.io.Serializable{
    private final String name;
    private final int population;
    private final Map<String, Property> entityProperties;

    public void setFinalPopulation(int finalPopulation) {
        this.finalPopulation = finalPopulation;
    }

    private  int finalPopulation;

    public int getFinalPopulation() {
        return finalPopulation;
    }



    public EntityDefinition(String name, Map<String, Property> entityProperties, int population) {
        this.name = name;
        this.entityProperties = entityProperties;
        this.population = population;
        this.finalPopulation = population;
    }

    public String getName() {
        return name;
    }

    public Property getPropertyByName(String name) {
        return this.entityProperties.get(name);
    }

    @Override
    public String toString() {
        return "Entity: " + name +
                ", Population: " + population +
                ", Properties: " + entityProperties.values();
    }

    public List<Entity> createEntityList() {
        return EntityFactory.createEntityList(this);
    }

    public int getPopulation() {
        return population;
    }

    public Map<String, Property> getProperties() {
        return entityProperties;
    }
}
