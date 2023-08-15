package engine.rule.action.expression;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;
import engine.world.World;

public interface Expression {

    Object evaluate(World world, Entity entity);
}
