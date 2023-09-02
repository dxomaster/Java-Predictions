package rule.action.expression;

import entity.Entity;
import world.World;
import world.utils.PropertyType;

public interface Expression {
    boolean isNotNumber();
    Object evaluate(World world, Entity entity,Entity secondaryEntity);
    PropertyType getType();
}
