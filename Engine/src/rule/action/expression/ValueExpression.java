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
    public boolean isNotNumber() {
        return propertyType != PropertyType.FLOAT && propertyType != PropertyType.DECIMAL;
    }

    @Override
    public Object evaluate(World world, Entity entity,Entity secondaryEntity){
        return value;
    }
    @Override
    public PropertyType getType() {
        return propertyType;
    }

    @Override
    public String toString() {
        return "Value Expression:" +
                " Value:" + value;
    }
}
