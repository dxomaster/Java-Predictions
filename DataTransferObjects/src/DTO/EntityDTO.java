package DTO;

import java.util.List;

public class EntityDTO {
    public String getName() {
        return name;
    }

    private String name;

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    private List<PropertyDTO> properties;

    public int getPopulation() {
        return population;
    }

    private int population;

    public EntityDTO(String name, List<PropertyDTO> properties, int population) {
        this.name = name;
        this.properties = properties;
        this.population = population;
    }

    public void setPopulation(int newPopulation) {
        this.population = newPopulation;
    }
}
