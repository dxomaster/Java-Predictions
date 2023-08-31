package rule.action.expression;

import entity.Entity;
import entity.EntityDefinition;
import world.World;
import world.utils.Property;
import world.utils.PropertyType;

public class PropertyExpression implements Expression, java.io.Serializable {
    private final Property property;

    public PropertyExpression(EntityDefinition entityDefinition, String propertyName) {
        this.property = entityDefinition.getPropertyByName(propertyName);


    }

    @Override
    public Object evaluate(World world, Entity entity) {

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
