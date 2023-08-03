package engine.rule.action.condition;

import engine.entity.Entity;

import java.util.List;

public class MultipleCondition implements Satisfiable {
    private LogicalOperator operator;
    List<Satisfiable> conditions;
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
}
