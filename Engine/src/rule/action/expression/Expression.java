package rule.action.expression;

import entity.Entity;
import world.World;
import world.utils.PropertyType;

public interface Expression {

    Object evaluate(World world, Entity entity);
    PropertyType getType();
}
