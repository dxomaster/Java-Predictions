package engine.rule.action.condition;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;
import engine.world.World;

import java.util.List;

public interface Satisfiable {
    boolean isSatisfied(World world, Entity entity) throws ErrorException;

    List<String> getEntities();
}
