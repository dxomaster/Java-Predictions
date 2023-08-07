package engine.rule.action.expression;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.world.utils.Property;

public class PropertyExpression implements Expression{
    private final Property property;
    @Override
    public Object evaluate(Entity entity) {

        return entity.getPropertyByName(property.getName()).getValue();
    }
    public PropertyExpression(EntityDefinition entityDefinition, String propertyName) {
        this.property = entityDefinition.getPropertyByName(propertyName);


    }

    @Override
    public String toString() {
        return "PropertyExpression{" +
                "property=" + property +
                '}';
    }
}
