package engine.rule.action.condition;

import engine.entity.Entity;
import engine.rule.action.Actionable;

import java.util.List;

public class MultipleCondition implements Satisfiable, Actionable {
    List<Actionable> actionsToPreformIfConditionIsSatisfied;
    List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    private LogicalOperator operator;
    List<Satisfiable> conditions;
    public MultipleCondition(LogicalOperator operator, List<Satisfiable> conditions, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied) {
        this.operator = operator;
        this.conditions = conditions;
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
    }
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
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(entity);
            }
        }
        else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(entity);
            }
        }
    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "actionsToPreformIfConditionIsSatisfied=" + actionsToPreformIfConditionIsSatisfied +
                ", actionsToPreformIfConditionIsNotSatisfied=" + actionsToPreformIfConditionIsNotSatisfied +
                ", operator=" + operator +
                ", conditions=" + conditions +
                '}';
    }
}