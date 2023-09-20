package DTO;

import java.util.List;

public class EntityDTO {
    public String getName() {
        return name;
    }

    private String name;

    public int getCurrentPopulation() {
        return currentPopulation;
    }

    private int currentPopulation;

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    private List<PropertyDTO> properties;

    public int getPopulation() {
        return population;
    }

    private int population;

    public EntityDTO(String name, List<PropertyDTO> properties, int population, int currentPopulation) {
        this.name = name;
        this.properties = properties;
        this.population = population;
        this.currentPopulation = currentPopulation;
    }

    public void setPopulation(int newPopulation) {
        this.population = newPopulation;
    }
}
