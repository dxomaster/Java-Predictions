package world.utils;

public enum PropertyType implements java.io.Serializable {
    DECIMAL(Integer.class),
    FLOAT(Float.class),
    BOOLEAN(Boolean.class),
    STRING(String.class);
    public final Class propertyClass;

    PropertyType(Class propertyClass) {
        this.propertyClass = propertyClass;
    }

}
