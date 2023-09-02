package rule.action.expression;

import entity.Entity;
import entity.EntityDefinition;
import world.World;
import world.utils.Property;
import world.utils.PropertyType;

public class PropertyExpression implements Expression, java.io.Serializable {
    private final String entityName;
    private final Property property;

    public PropertyExpression(EntityDefinition entityDefinition, String propertyName) {
        this.property = entityDefinition.getPropertyByName(propertyName);
        this.entityName = entityDefinition.getName();

    }

    @Override
    public boolean isNotNumber() {
        return property.getType() != PropertyType.FLOAT && property.getType() != PropertyType.DECIMAL;
    }

    @Override
    public Object evaluate(World world, Entity entity,Entity secondaryEntity) {
        if(secondaryEntity !=null && secondaryEntity.getName().equals(entityName))
            return secondaryEntity.getPropertyByName(property.getName()).getValue();
        return entity.getPropertyByName(property.getName()).getValue();
    }
    @Override
    public PropertyType getType() {
        return property.getType();
    }

    @Override
    public String toString() {
        return "PropertyExpression{" +
                "property=" + property +
                '}';
    }
}
