package DTO;

import java.util.List;

public class StatisticEntityDTO implements java.io.Serializable {
    private final List<StatisticPropertyDTO> properties;
    private final String name;

    public List<Integer> getPopulationOverTime() {
        return populationOverTime;
    }

    private final List<Integer> populationOverTime;
    private final int population;

    public StatisticEntityDTO(List<StatisticPropertyDTO> properties, String name, int population, List<Integer> populationOverTime) {
        this.properties = properties;
        this.name = name;
        this.population = population;
        this.populationOverTime = populationOverTime;

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
        return this.populationOverTime.get(this.populationOverTime.size() - 1);
    }

    public List<StatisticPropertyDTO> getPropertyDTOList() {
        return this.properties;
    }
}
