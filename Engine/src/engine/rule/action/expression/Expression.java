package engine.rule.action.expression;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;

public interface Expression {
    Object evaluate(Entity entity) throws ErrorException;
}
