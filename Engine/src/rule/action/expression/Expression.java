package rule.action.expression;

import entity.Entity;
import world.World;

public interface Expression {

    Object evaluate(World world, Entity entity);
}
