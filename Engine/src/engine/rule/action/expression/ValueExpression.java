package engine.rule.action.expression;

import engine.entity.Entity;
import engine.world.World;
import engine.world.utils.PropertyType;

public class ValueExpression implements Expression {
    private final PropertyType propertyType;
    private final Object value;

    public ValueExpression(Object value, PropertyType propertyType) {
        this.propertyType = propertyType;
        this.value = value;
    }

    @Override
    public Object evaluate(World world, Entity entity) {
        return value;
    }

    public PropertyType getType() {
        return propertyType;
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "value=" + value +
                '}';
    }
}
