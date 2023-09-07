package DTO;

import java.util.Map;

public class StatisticPropertyDTO implements java.io.Serializable {
    private final String name;
    private final String type;
    private final Map<String, Integer> valueFrequency;
    private final Float consistency;
    private final Float average;

    public StatisticPropertyDTO(String name, String type, Map<String, Integer> valueFrequency, Float consistency, Float average) {
        this.name = name;
        this.type = type;
        this.valueFrequency = valueFrequency;
        this.consistency = consistency;
        this.average = average;
    }

    public Map<String, Integer> getValueFrequency() {
        return valueFrequency;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
