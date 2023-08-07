package engine.rule.action.condition;

import engine.entity.Entity;

public interface Satisfiable {
     boolean isSatisfied(Entity entity);
}
