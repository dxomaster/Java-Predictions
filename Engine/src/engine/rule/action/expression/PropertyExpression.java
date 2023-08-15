package engine.rule.action.expression;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.world.World;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;

public class PropertyExpression implements Expression, java.io.Serializable {
    private final Property property;

    public PropertyExpression(EntityDefinition entityDefinition, String propertyName) {
        this.property = entityDefinition.getPropertyByName(propertyName);


    }

    @Override
    public Object evaluate(World world, Entity entity) {

        return entity.getPropertyByName(property.getName()).getValue();
    }

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
