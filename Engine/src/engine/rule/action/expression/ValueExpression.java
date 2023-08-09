package engine.rule.action.expression;

import engine.entity.Entity;
import engine.world.utils.PropertyType;

public class ValueExpression implements Expression{
    private PropertyType propertyType;
    private Object value;
    @Override
    public Object evaluate(Entity entity) {
        return value;
    }
    public ValueExpression(Object value, PropertyType propertyType) {
        this.propertyType = propertyType;
        this.value = value;
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
