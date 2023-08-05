package engine.rule.action.condition;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.Action;
import engine.rule.action.Actionable;
import engine.rule.action.expression.Expression;
import engine.world.utils.PropertyType;

import java.util.List;

public class SimpleCondition implements Satisfiable {
    private String property;
    private EntityDefinition entityDefinition;
    private ConditionOperator operator;
    private Expression expression;
    @Override
    public boolean isSatisfied(Entity entity) {
        switch (operator) {
            case EQUALS:
                return entity.getPropertyByName(property).getValue().equals(expression.evaluate(entity));
            case NOT_EQUALS:
                return !entity.getPropertyByName(property).getValue().equals(expression.evaluate(entity));
            case LESS_THAN:
                return (float)entity.getPropertyByName(property).getValue() < (float)expression.evaluate(entity);
            case GREATER_THAN:
                return (float)entity.getPropertyByName(property).getValue() > (float)expression.evaluate(entity);
            default:
                return false;
        }
    }

    public SimpleCondition(EntityDefinition entityDefinition, Expression expression,String property,ConditionOperator operator) {
        this.expression = expression;
        this.property = property;
        this.entityDefinition = entityDefinition;
        this.operator = operator;
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
    public String toString() {
        return "SimpleCondition{" +
                "property='" + property + '\'' +
                ", entityDefinition=" + entityDefinition +
                ", operator=" + operator +
                ", expression=" + expression +
                '}';
    }
}
