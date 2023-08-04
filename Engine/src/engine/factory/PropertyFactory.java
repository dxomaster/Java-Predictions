package engine.factory;

import engine.jaxb.schema.generated.*;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;
import engine.world.utils.Range;

import java.util.ArrayList;
import java.util.List;

public class PropertyFactory {
    public static Property createProperty(PRDProperty prdProperty) {
        return createProperty(prdProperty.getPRDRange(), prdProperty.getType(), prdProperty.getPRDName());
    }
    public static List<Property> createPropertyList(PRDEntity prdEntity)
    {
        List<PRDProperty> prdProperties = prdEntity.getPRDProperties().getPRDProperty();
        List<Property> propertyList = new ArrayList<>();
        for (PRDProperty prdProperty : prdProperties) {
            Property property = PropertyFactory.createProperty(prdProperty);
            propertyList.add(property);
        }
        return propertyList;
    }
    public static List<Property> createPropertyList(List<PRDEnvProperty> prdEnvProperties) {
        List<Property> propertyList = new ArrayList<>();
        for (PRDEnvProperty prdEnvProperty : prdEnvProperties) {
            Property property = PropertyFactory.createProperty(prdEnvProperty);
            propertyList.add(property);
        }
        return propertyList;
    }
    public static Property createProperty(PRDEnvProperty prdEnvProperty) {
        return createProperty(prdEnvProperty.getPRDRange(), prdEnvProperty.getType(), prdEnvProperty.getPRDName());
    }

    private static Property createProperty(PRDRange prdRange, String type2, String prdName) {
        Range range = RangeFactory.createRange(prdRange);
        String type = type2;
        switch(type){
            case "decimal":
                return new Property(prdName, PropertyType.DECIMAL, range);
            case "float":
                return new Property(prdName, PropertyType.FLOAT, range);
            case "boolean":
                return new Property(prdName, PropertyType.BOOLEAN, range);
            case "string":
                return new Property(prdName, PropertyType.STRING, range);
            default:
                throw new IllegalArgumentException("Invalid property type: " + type);
        }
    }
}
