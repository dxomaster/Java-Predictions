package DTO;

import java.util.List;

public class EntityDTO implements java.io.Serializable {
    private final List<PropertyDTO> properties;
    private final String name;
    private final int population;
    private final int finalPopulation;

    public EntityDTO(List<PropertyDTO> properties, String name, int population, int finalPopulation) {
        this.properties = properties;
        this.name = name;
        this.population = population;
        this.finalPopulation = finalPopulation;

    }


    public int getPopulation() {
        return population;
    }

    public String getName() {
        return name;
    }

    public String[] getProperties() {
        String[] properties = new String[this.properties.size()];
        for (int i = 0; i < this.properties.size(); i++) {
            properties[i] = this.properties.get(i).getName();
        }
        return properties;
    }

    public int getFinalPopulation() {
        return finalPopulation;
    }

    public List<PropertyDTO> getPropertyDTOList() {
        return this.properties;
    }
}
