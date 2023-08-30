package entity;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import world.utils.Property;
import world.utils.PropertyType;

import java.util.Map;

public class Entity implements java.io.Serializable {
    private final String name;
    private final Map<String, Property> entityProperties;

    boolean toKill = false;

    public Entity(String name, Map<String, Property> entityProperties) {
        this.name = name;
        this.entityProperties = entityProperties;
    }


    @Override
    public String toString() {
        return "Entity{" + entityProperties + "}";
    }

    public String getName() {
        return name;
    }

    public Property getPropertyByName(String name) {
        return this.entityProperties.get(name);
    }

    public void increaseProperty(String propertyNameInString, Object evaluate, int tick) throws WarnException {

        Property property = getPropertyByName(propertyNameInString);
        Object currentValue = property.getValue();

        if (property.getType() == PropertyType.DECIMAL)
            property.setValue((Integer) currentValue + (Integer) evaluate, tick);
        else
            property.setValue((Float) currentValue + (Float) evaluate, tick);
    }


    public void decreaseProperty(String propertyNameInString, Object evaluate, int tick) throws WarnException {

        Property property = getPropertyByName(propertyNameInString);
        Object currentValue = property.getValue();
        if (property.getType() == PropertyType.DECIMAL)
            property.setValue((Integer) currentValue - (Integer) evaluate, tick);
        else
            property.setValue((Float) currentValue - (Float) evaluate, tick);
    }


    public void setProperty(String propertyNameInString, Object evaluate, int tick) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            property.setValue(evaluate, tick);
        } catch (WarnException ignored) {

        }
    }

    public void multiplyProperty(String propertyNameInString, Object evaluate1, Object evaluate2, int tick) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            if (property.getType() == PropertyType.DECIMAL)
                property.setValue((Integer) evaluate1 * (Integer) evaluate2,tick);
            else
                property.setValue((Float) evaluate1 * (Float) evaluate2, tick);
        } catch (WarnException ignored) {

        }
    }

    public void kill() {
        toKill = true;
    }

    public boolean getToKill() {
        return toKill;
    }

    public void divideProperty(String propertyNameInString, Object evaluate, Object evaluate1, int tick) throws ErrorException, WarnException {

        Property property = getPropertyByName(propertyNameInString);
        if (property.getType() == PropertyType.DECIMAL) {
            if ((Integer) evaluate1 == 0)
                throw new ErrorException("Division by zero while running");
            property.setValue((Integer) evaluate / (Integer) evaluate1, tick);
        } else if ((Float) evaluate1 == 0)
            throw new ErrorException("Division by zero while running");
        else
            property.setValue((Float) evaluate / (Float) evaluate1, tick);

    }

}
