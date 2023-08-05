package engine.rule.action.expression;

import engine.entity.Entity;

public class ValueExpression implements Expression{
    private Object value;
    @Override
    public Object evaluate(Entity entity) {
        return value;
    }
    public ValueExpression(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "value=" + value +
                '}';
    }
}
