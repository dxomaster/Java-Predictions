package engine.rule.action.condition;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.Action;
import engine.rule.action.Actionable;
import engine.rule.action.expression.Expression;
import engine.world.utils.PropertyType;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.List;

public class Condition implements Satisfiable, Actionable {
    List<Actionable> actionsToPreformIfConditionIsSatisfied;
    List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    SimpleCondition simpleCondition;
    @Override
    public boolean isSatisfied(Entity entity) {
        return simpleCondition.isSatisfied(entity);
    }

    public Condition(SimpleCondition simpleCondition, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied) {
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
        this.simpleCondition = simpleCondition;
    }

    @Override
    public void performAction(Entity entity) {
        if (isSatisfied(entity)) {
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(entity);
            }
        } else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(entity);
            }
        }
    }
}
