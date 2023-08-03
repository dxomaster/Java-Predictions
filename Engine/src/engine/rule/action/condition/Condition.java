package engine.rule.action.condition;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.expression.Expression;
import engine.world.utils.PropertyType;

public class Condition implements Satisfiable {
    private String property;
    private EntityDefinition entityDefinition;
    private ConditionOperator operator;
    private Expression expression;
    @Override
    public boolean isSatisfied(Entity entity) {
        return false;
    }

    public Condition(String property, EntityDefinition entityDefinition, ConditionOperator operator, Expression expression) {
        if (entityDefinition.getPropertyByName(property) == null) {
            throw new IllegalArgumentException("Property " + property + " does not exist in " + entityDefinition);
        }
        if (operator ==  ConditionOperator.LESS_THAN || ConditionOperator.GREATER_THAN == operator) {
            PropertyType entityType = entityDefinition.getPropertyByName(property).getType();
            if (entityType != PropertyType.FLOAT && entityType != PropertyType.DECIMAL){
                throw new IllegalArgumentException("Property " + property + " is not a number");
            }
        }
        this.expression = expression;
        this.property = property;
        this.entityDefinition = entityDefinition;

    }
}
