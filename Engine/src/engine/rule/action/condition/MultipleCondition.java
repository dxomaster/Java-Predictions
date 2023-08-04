package engine.rule.action.condition;

import engine.entity.Entity;
import engine.rule.action.Actionable;

import java.util.List;

public class MultipleCondition implements Satisfiable, Actionable {
    private LogicalOperator operator;
    List<Condition> conditions;
    @Override
    public boolean isSatisfied(Entity entity) {
        switch (operator)
        {
            case AND:
                for (Satisfiable condition : conditions) {
                    if (!condition.isSatisfied(entity)) {
                        return false;
                    }
                }
                return true;
            case OR:
                for (Satisfiable condition : conditions) {
                    if (condition.isSatisfied(entity)) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public void performAction(Entity entity) {
        if (isSatisfied(entity)) {
            for (Actionable condition : conditions) {
                condition.performAction(entity);
            }
        }
    }
}
