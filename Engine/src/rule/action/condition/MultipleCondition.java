package rule.action.condition;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import rule.action.Actionable;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition implements Satisfiable, Actionable, java.io.Serializable {
    private final List<Actionable> actionsToPreformIfConditionIsSatisfied;
    private final List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    private final LogicalOperator operator;
    private final List<Satisfiable> conditions;

    public MultipleCondition(LogicalOperator operator, List<Satisfiable> conditions, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied) {
        this.operator = operator;
        this.conditions = conditions;
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
    }

    @Override
    public boolean isSatisfied(World world, Entity entity) throws ErrorException {
        switch (operator) {
            case AND:
                for (Satisfiable condition : conditions) {
                    if (!condition.isSatisfied(world, entity)) {
                        return false;
                    }
                }
                return true;
            case OR:
                for (Satisfiable condition : conditions) {
                    if (condition.isSatisfied(world, entity)) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public void performAction(World world, Entity entity) throws WarnException, ErrorException {
        if (isSatisfied(world, entity)) {
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(world, entity);
            }
        } else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(world, entity);
            }
        }
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        for (Satisfiable condition : conditions) {
            entities.addAll(condition.getEntities());
        }
        return entities;
    }

    @Override
    public String getName() {
        return "multiple condition";
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
