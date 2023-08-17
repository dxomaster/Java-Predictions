package engine.entity;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;

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

    public void increaseProperty(String propertyNameInString, Object evaluate) throws WarnException {

        Property property = getPropertyByName(propertyNameInString);
        Object currentValue = property.getValue();

        if (property.getType() == PropertyType.DECIMAL)
            property.setValue((Integer) currentValue + (Integer) evaluate);
        else
            property.setValue((Float) currentValue + (Float) evaluate);
    }


    public void decreaseProperty(String propertyNameInString, Object evaluate) throws WarnException {

        Property property = getPropertyByName(propertyNameInString);
        Object currentValue = property.getValue();
        if (property.getType() == PropertyType.DECIMAL)
            property.setValue((Integer) currentValue - (Integer) evaluate);
        else
            property.setValue((Float) currentValue - (Float) evaluate);
    }


    public void setProperty(String propertyNameInString, Object evaluate) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            property.setValue(evaluate);
        } catch (WarnException ignored) {

        }
    }

    public void multiplyProperty(String propertyNameInString, Object evaluate1, Object evaluate2) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            if (property.getType() == PropertyType.DECIMAL)
                property.setValue((Integer) evaluate1 * (Integer) evaluate2);
            else
                property.setValue((Float) evaluate1 * (Float) evaluate2);
        } catch (WarnException ignored) {

        }
    }

    public void kill() {
        toKill = true;
    }

    public boolean getToKill() {
        return toKill;
    }

    public void divideProperty(String propertyNameInString, Object evaluate, Object evaluate1) throws ErrorException, WarnException {

        Property property = getPropertyByName(propertyNameInString);
        if (property.getType() == PropertyType.DECIMAL) {
            if ((Integer) evaluate1 == 0)
                throw new ErrorException("Division by zero while running");
            property.setValue((Integer) evaluate / (Integer) evaluate1);
        } else if ((Float) evaluate1 == 0)
            throw new ErrorException("Division by zero while running");
        else
            property.setValue((Float) evaluate / (Float) evaluate1);

    }

}
