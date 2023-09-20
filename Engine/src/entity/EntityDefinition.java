package entity;

import DTO.EntityDTO;
import DTO.PropertyDTO;
import Exception.WARN.WarnException;
import factory.EntityFactory;
import world.utils.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityDefinition implements java.io.Serializable {
    private final String name;
    private int population;
    private int currentPopulation;
    private Map<String, Property> entityProperties = new java.util.HashMap<>();


    public EntityDefinition(String name, Map<String, Property> entityProperties, int population) {
        this.name = name;
        this.entityProperties = entityProperties;
        this.population = population;
        this.currentPopulation = population;
    }

    public EntityDefinition(EntityDefinition entityDefinition) {
        for (Property property : entityDefinition.entityProperties.values()) {
            this.entityProperties.put(property.getName(), new Property(property));
        }
        this.name = entityDefinition.name;
        this.population = new Integer(entityDefinition.population);
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
                "\nPopulation: " + population +
                "\nProperties: " + entityProperties.values();
    }

    public List<Entity> createEntityList() throws WarnException {
        return EntityFactory.createEntityList(this);
    }

    public int getPopulation() {
        return population;
    }

    public Map<String, Property> getProperties() {
        return entityProperties;
    }

    public EntityDTO getEntityDTO() {
        List<PropertyDTO> entityProperties = new ArrayList<>();
        for (Property property : this.entityProperties.values()) {
            entityProperties.add(property.getPropertyDTO());
        }
        return new EntityDTO(name, entityProperties, population, currentPopulation);
    }

    public void setPopulation(Integer newValue) {
        this.population = newValue;
    }
    public void setCurrentPopulation(Integer newValue) {
        this.currentPopulation = newValue;
    }
}
