package engine.rule.action;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;

import java.util.List;

public interface Actionable {
    void performAction(Entity entity) throws WarnException, ErrorException;

    List<String> getEntities();

    String getName();
}
