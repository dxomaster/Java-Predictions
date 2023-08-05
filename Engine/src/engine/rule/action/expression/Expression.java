package engine.rule.action.expression;

import engine.entity.Entity;

public interface Expression {
    public Object evaluate(Entity entity);
}
