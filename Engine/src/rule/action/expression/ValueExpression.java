package rule.action.expression;

import entity.Entity;
import world.World;
import world.utils.PropertyType;

public class ValueExpression implements Expression, java.io.Serializable {
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
