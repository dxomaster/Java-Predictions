package engine.rule.action.condition;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;

import java.util.List;

public interface Satisfiable {
    boolean isSatisfied(Entity entity) throws ErrorException;

    List<String> getEntities();
}
