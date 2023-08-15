package DTO;

import java.util.Map;

public class PropertyDTO implements java.io.Serializable{
    private final String name;
    private final String type;

    public Map<String, Integer> getValueFrequency() {
        return valueFrequency;
    }

    private final Map<String,Integer> valueFrequency;

    public PropertyDTO(String name, String type, Map<String,Integer> valueFrequency) {
        this.name = name;
        this.type = type;
        this.valueFrequency = valueFrequency;


    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
