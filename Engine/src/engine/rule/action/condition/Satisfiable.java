package engine.rule.action.condition;

import engine.entity.Entity;

import java.util.List;

public interface Satisfiable {
     boolean isSatisfied(Entity entity);
     List<String> getEntities();
}
