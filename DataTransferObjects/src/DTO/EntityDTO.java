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

    public EntityDTO(String name, List<PropertyDTO> properties) {
        this.name = name;
        this.properties = properties;
    }

}
