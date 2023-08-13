package DTO;

public class EnvDTO {
    private final String name;
    private final Class type;
    private Object value;
    private final Object from;
    private final Object to;
    public EnvDTO(String name, Class type, Object value, Object from, Object to) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        try {
            if (type == Integer.class)
                this.value = Integer.parseInt(value);
            else if (type == Float.class)
                this.value = Float.parseFloat(value);
            else if (type == Boolean.class)
                switch (value.toLowerCase()) {
                    case "true":
                        this.value = true;
                        break;
                    case "false":
                        this.value = false;
                        break;
                    default:
                        throw new IllegalArgumentException("Value does not match type");
                }
            else
                this.value = value;
        } catch (Exception e) {
            throw new IllegalArgumentException("Value does not match type");
        }
    }

    public String getFrom() {
        if (from == null)
            return null;
        return from.toString();
    }

    public String getTo() {
        if (to == null)
            return null;
        return to.toString();
    }
}
