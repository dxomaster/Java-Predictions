package engine.rule.action.condition;

import engine.entity.Entity;

public interface Satisfiable {
    public boolean isSatisfied(Entity entity);
}
