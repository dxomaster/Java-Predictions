package engine.rule.action.expression;

import engine.entity.Entity;

public class PropertyExpression implements Expression{
    private String property;
    private Entity entity;
    @Override
    public Object evaluate() {
        return entity.getPropertyByName(property).getValue();
    }
    public PropertyExpression(Entity entity, String property) {
        this.property = property;
        this.entity = entity;
    }
}
