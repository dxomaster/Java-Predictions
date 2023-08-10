package engine.rule.action.condition;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.expression.Expression;
import engine.rule.action.expression.FunctionExpression;
import engine.rule.action.expression.PropertyExpression;
import engine.rule.action.expression.ValueExpression;
import engine.world.utils.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class SimpleCondition implements Satisfiable {
    private final String property;
    private final EntityDefinition entityDefinition;
    private final ConditionOperator operator;
    private final Expression expression;

    @Override
    public boolean isSatisfied(Entity entity) throws ErrorException {
        Object comparisonValue = expression.evaluate(entity);
        Object entityValue = entity.getPropertyByName(property).getValue();
        PropertyType type;
        if (expression instanceof ValueExpression) {
            type = ((ValueExpression) expression).getType();
        } else if (expression instanceof PropertyExpression) {
            type = ((PropertyExpression) expression).getType();
        } else {
            type = ((FunctionExpression) expression).getType();
        }
        if (type == PropertyType.FLOAT) {
            return compareFloat((Float) entityValue, (Float) comparisonValue);
        } else if (type == PropertyType.DECIMAL) {
            return compareInteger((Integer) entityValue, (Integer) comparisonValue);
        } else if (type == PropertyType.BOOLEAN) {
            return compareBoolean((Boolean) entityValue, (Boolean) comparisonValue);
        } else {
            return compareStrings((String) entityValue, (String) comparisonValue);
        }


    }

    private boolean compareBoolean(Boolean entityValue, Boolean comparisonValue) {
        switch (operator) {
            case EQUALS:
                return entityValue.equals(comparisonValue);
            case NOT_EQUALS:
                return !entityValue.equals(comparisonValue);
            default:
                return false;
        }
    }

    private boolean compareStrings(String entityValue, String comparisonValue) {
        switch (operator) {
            case EQUALS:
                return entityValue.equals(comparisonValue);
            case NOT_EQUALS:
                return !entityValue.equals(comparisonValue);
            default:
                return false;
        }
    }

    private boolean compareFloat(Float entityValue, Float comparisonValue) {
        switch (operator) {
            case EQUALS:
                return entityValue.equals(comparisonValue);
            case NOT_EQUALS:
                return !entityValue.equals(comparisonValue);
            default:
                return false;
        }
    }

    private boolean compareInteger(Integer entityValue, Integer comparisonValue) {
        switch (operator) {
            case EQUALS:
                return entityValue.equals(comparisonValue);
            case NOT_EQUALS:
                return !entityValue.equals(comparisonValue);
            case LESS_THAN:
                return entityValue < comparisonValue;
            case GREATER_THAN:
                return entityValue > comparisonValue;
            default:
                return false;
        }
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(entityDefinition.getName());
        return entities;
    }


    public SimpleCondition(EntityDefinition entityDefinition, Expression expression, String property, ConditionOperator operator) {
        this.expression = expression;
        this.property = property;
        this.entityDefinition = entityDefinition;
        this.operator = operator;
        if (this.entityDefinition.getPropertyByName(property) == null) {
            throw new IllegalArgumentException("Property " + property + " does not exist in " + entityDefinition);
        }
        if (operator == ConditionOperator.LESS_THAN || ConditionOperator.GREATER_THAN == operator) {
            PropertyType entityType = this.entityDefinition.getPropertyByName(property).getType();
            if (entityType != PropertyType.FLOAT && entityType != PropertyType.DECIMAL) {
                throw new IllegalArgumentException("Property " + property + " is not a number");
            }
        }

    }

    public String getEntityName() {
        return entityDefinition.getName();
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
