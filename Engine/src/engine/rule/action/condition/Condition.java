package engine.rule.action.condition;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.Action;
import engine.rule.action.Actionable;
import engine.rule.action.expression.Expression;
import engine.world.utils.PropertyType;

import java.util.List;

public class Condition implements Satisfiable, Actionable {
    List<Action> actionsToPreformIfConditionIsSatisfied;
    List<Action> actionsToPreformIfConditionIsNotSatisfied;
    private String property;
    private EntityDefinition entityDefinition;
    private ConditionOperator operator;
    private Expression expression;
    @Override
    public boolean isSatisfied(Entity entity) {
        switch (operator) {
            case EQUALS:
                return entity.getPropertyByName(property).getValue().equals(expression.evaluate());
            case NOT_EQUALS:
                return !entity.getPropertyByName(property).getValue().equals(expression.evaluate());
            case LESS_THAN:
                return (float)entity.getPropertyByName(property).getValue() < (float)expression.evaluate();
            case GREATER_THAN:
                return (float)entity.getPropertyByName(property).getValue() > (float)expression.evaluate();
            default:
                return false;
        }
    }

    public Condition(EntityDefinition entityDefinition, Expression expression,String property,ConditionOperator operator, List<Action> actionsToPreformIfConditionIsSatisfied, List<Action> actionsToPreformIfConditionIsNotSatisfied) {
        this.expression = expression;
        this.property = property;
        this.entityDefinition = entityDefinition;
        this.operator = operator;
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;

        if (this.entityDefinition.getPropertyByName(property) == null) {
            throw new IllegalArgumentException("Property " + property + " does not exist in " + entityDefinition);
        }
        if (operator ==  ConditionOperator.LESS_THAN || ConditionOperator.GREATER_THAN == operator) {
            PropertyType entityType = this.entityDefinition.getPropertyByName(property).getType();
            if (entityType != PropertyType.FLOAT && entityType != PropertyType.DECIMAL){
                throw new IllegalArgumentException("Property " + property + " is not a number");
            }
        }

    }

    @Override
    public void performAction(Entity entity) {
        if (isSatisfied(entity)) {
            for (Action action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(entity);
            }
        } else {
            for (Action action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(entity);
            }
        }
    }
}
