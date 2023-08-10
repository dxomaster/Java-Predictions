package DTO;
public class EnvDTO {
    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    private String name;
    private Class type;
    private Object value;

    public EnvDTO(String name, Class type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
