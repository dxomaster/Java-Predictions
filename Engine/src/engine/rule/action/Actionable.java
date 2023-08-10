package engine.rule.action;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.world.World;

import java.util.List;

public interface Actionable {
    void performAction(World world, Entity entity) throws WarnException, ErrorException;

    List<String> getEntities();

    String getName();
}
