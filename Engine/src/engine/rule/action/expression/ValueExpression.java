package engine.rule.action.expression;

public class ValueExpression implements Expression{
    private Object value;
    @Override
    public Object evaluate() {
        return value;
    }
    public ValueExpression(Object value) {
        this.value = value;
    }
}
