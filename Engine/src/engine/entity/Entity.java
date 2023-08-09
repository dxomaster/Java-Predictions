package engine.entity;

import Exception.WARN.WarnException;
import engine.world.World;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;
import java.util.Map;

public class Entity {// todo: population
    private final String name;
    private final Map<String, Property> entityProperties;

    public Entity(String name, Map<String, Property> entityProperties) {
        this.name = name;
        this.entityProperties = entityProperties;
    }

    public Map<String, Property> getEntityProperties() {
        return entityProperties;
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



    public void decreaseProperty(String propertyNameInString, Object evaluate) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            Object currentValue = property.getValue();
            if (property.getType() == PropertyType.DECIMAL)
                property.setValue((Integer) currentValue - (Integer) evaluate);
            else
                property.setValue((Float) currentValue - (Float) evaluate);
        } catch (WarnException ignored) {

        }
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
        World.RemoveEntity(this);
    }

    public void divideProperty(String propertyNameInString, Object evaluate, Object evaluate1) {
        try {
            Property property = getPropertyByName(propertyNameInString);
            if (property.getType() == PropertyType.DECIMAL)
                property.setValue((Integer) evaluate / (Integer) evaluate1);
            else
                property.setValue((Float) evaluate / (Float) evaluate1);
        } catch (WarnException ignored) {

        }
    }
}
