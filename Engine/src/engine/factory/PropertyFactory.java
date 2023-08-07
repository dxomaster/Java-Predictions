package engine.factory;

import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.*;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;
import engine.world.utils.Range;

import java.util.*;

public class PropertyFactory {
    public static Property createEntityProperty(PRDProperty prdProperty) {
        if(prdProperty.getPRDRange() != null)
        {
            return createEntityPropertyWithRange(prdProperty);
        }
        else
        {
            return createEntityPropertyWithoutRange(prdProperty);
        }

    }

    private static Property createEntityPropertyWithRange(PRDProperty prdProperty) {
        Range range = RangeFactory.createRange(prdProperty.getPRDRange(), PropertyType.valueOf(prdProperty.getType().toUpperCase()));
        String type = prdProperty.getType();
        boolean randomInit = prdProperty.getPRDValue().isRandomInitialize();
        if (randomInit) {

            return createRandomInitEntityProperty(prdProperty.getPRDName(), range, type);
        }
        else
        {
            if (isInvalid(prdProperty.getPRDValue().getInit(), type)) {
                throw new IllegalArgumentException("Invalid init value for type: " + type);
            }
            switch (type) {
                case "decimal":
                    return new Property(prdProperty.getPRDName(), PropertyType.DECIMAL, range, Integer.parseInt(prdProperty.getPRDValue().getInit()));
                case "float":
                    return new Property(prdProperty.getPRDName(), PropertyType.FLOAT, range, Float.parseFloat(prdProperty.getPRDValue().getInit()));
                case "boolean":
                    return new Property(prdProperty.getPRDName(), PropertyType.BOOLEAN, range, Boolean.parseBoolean(prdProperty.getPRDValue().getInit()));
                case "string":
                    return new Property(prdProperty.getPRDName(), PropertyType.STRING, range, prdProperty.getPRDValue().getInit());
                default:
                    throw new IllegalArgumentException("Invalid property type: " + type);
            }
        }

    }

    private static Property createEntityPropertyWithoutRange(PRDProperty prdProperty) {
        String type = prdProperty.getType();
        boolean randomInit = prdProperty.getPRDValue().isRandomInitialize();
        if (randomInit) {

            return createRandomInitEntityProperty(prdProperty.getPRDName(), null, type);
        }
        else
        {
            if (isInvalid(prdProperty.getPRDValue().getInit(), type)) {
                throw new IllegalArgumentException("Invalid init value for type: " + type);
            }
            switch (type) {
                case "decimal":
                    return new Property(prdProperty.getPRDName(), PropertyType.DECIMAL, null, Integer.parseInt(prdProperty.getPRDValue().getInit()));
                case "float":
                    return new Property(prdProperty.getPRDName(), PropertyType.FLOAT, null, Float.parseFloat(prdProperty.getPRDValue().getInit()));
                case "boolean":
                    return new Property(prdProperty.getPRDName(), PropertyType.BOOLEAN, null, Boolean.parseBoolean(prdProperty.getPRDValue().getInit()));
                case "string":
                    return new Property(prdProperty.getPRDName(), PropertyType.STRING, null, prdProperty.getPRDValue().getInit());
                default:
                    throw new IllegalArgumentException("Invalid property type: " + type);
            }
        }

    }

    public static Map<String,Property> createPropertyList(PRDEntity prdEntity)
    {
        String duplicate = isEntityPropertyNameUnique(prdEntity.getPRDProperties().getPRDProperty());
        if (duplicate != null)
        {
            throw new IllegalArgumentException("Problem with "+prdEntity.getName() +" Property name is not unique: " + duplicate);
        }
        List<PRDProperty> prdProperties = prdEntity.getPRDProperties().getPRDProperty();
        Map<String,Property> propertyList = new HashMap<>();
        for (PRDProperty prdProperty : prdProperties) {
            Property property = PropertyFactory.createEntityProperty(prdProperty);
            propertyList.put(property.getName(),property);
        }
        return propertyList;
    }
    public static Map<String,Property> createPropertyList(EntityDefinition entityDefinition){
        Map<String,Property> propertyList = new HashMap<>();
        for (Property property : entityDefinition.getProperties().values())  {
            propertyList.put(property.getName(),property);
        }
        return propertyList;
    }
    private static String isEntityPropertyNameUnique(List<PRDProperty> prdProperty)
    {
        List<String> propertyNames = new ArrayList<>();
        for (PRDProperty property : prdProperty) {

            propertyNames.add(property.getPRDName());
        }
        Set<String> set = new HashSet<>();

        for (String name: propertyNames){
            if (!set.add(name))
                return name;
        }

        return null;
    }
    public static List<Property> createPropertyList(List<PRDEnvProperty> prdEnvProperties) {
        List<Property> propertyList = new ArrayList<>();
        String duplicate = isEnvPropertyNameUnique(prdEnvProperties);
        if(duplicate != null)
        {
            throw new IllegalArgumentException("Environment variable name is not unique: " + duplicate);
        }
        for (PRDEnvProperty prdEnvProperty : prdEnvProperties) {

            Property property = PropertyFactory.createEnvProperty(prdEnvProperty);
            propertyList.add(property);
        }
        return propertyList;
    }
    private static String isEnvPropertyNameUnique(List<PRDEnvProperty> prdEnvProperty)
    {

        List<String> propertyNames = new ArrayList<>();
        for (PRDEnvProperty property : prdEnvProperty) {

            propertyNames.add(property.getPRDName());
        }
        Set<String> set = new HashSet<>();

        for (String name: propertyNames){
            if (!set.add(name))
                return name;
        }

        return null;
    }
    public static Property createEnvProperty(PRDEnvProperty prdEnvProperty) {

        if(prdEnvProperty.getPRDRange() != null)
        {
            return createRandomInitEntityProperty(prdEnvProperty.getPRDName(), RangeFactory.createRange(prdEnvProperty.getPRDRange(),PropertyType.valueOf(prdEnvProperty.getType().toUpperCase())), prdEnvProperty.getType());
        }
        else
        {
            return createRandomInitEntityProperty(prdEnvProperty.getPRDName(), null, prdEnvProperty.getType());
        }
    }

    private static Property createRandomInitEntityProperty(String prdName, Range range, String type) {
        switch (type) {
            case "decimal":
                return new Property(prdName, PropertyType.DECIMAL, range);
            case "float":
                return new Property(prdName, PropertyType.FLOAT, range);
            case "boolean":
                return new Property(prdName, PropertyType.BOOLEAN, range);
            case "string":
                return new Property(prdName, PropertyType.STRING, range);
            default:
                System.out.println(prdName + range + type);
                throw new IllegalArgumentException("Invalid property type: " + type);
        }
    }

    private static boolean isInvalid(String init, String type) {
        switch (type) {
            case "decimal":
                try {
                    Integer.parseInt(init);
                } catch (NumberFormatException e) {
                    return true;
                }
                return false;
            case "float":
                try {
                    Float.parseFloat(init);
                } catch (NumberFormatException e) {
                    return true;
                }
                return false;
            case "boolean":
                return !init.equals("true") && !init.equals("false");
            case "string":
                return false;
            default:
                throw new IllegalArgumentException("Invalid property type: " + type);
        }
    }
}
