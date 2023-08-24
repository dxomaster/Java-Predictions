package rule.action.condition;

import Exception.ERROR.ErrorException;
import entity.Entity;
import world.World;

import java.util.List;

public interface Satisfiable {
    boolean isSatisfied(World world, Entity entity) throws ErrorException;

    List<String> getEntities();
}
