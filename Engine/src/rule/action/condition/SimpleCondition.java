package rule.action.condition;

import DTO.SatisfiableDTO;
import DTO.SimpleConditionDTO;
import Exception.ERROR.ErrorException;
import entity.Entity;
import entity.EntityDefinition;
import rule.action.expression.Expression;
import rule.action.expression.FunctionExpression;
import rule.action.expression.PropertyExpression;
import rule.action.expression.ValueExpression;
import world.World;
import world.utils.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class SimpleCondition implements Satisfiable, java.io.Serializable {
    public String getProperty() {
        return property;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public ConditionOperator getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    private final String property;
    private final EntityDefinition entityDefinition;
    private final ConditionOperator operator;
    private final Expression expression;

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

    @Override
    public boolean isSatisfied(World world, Entity entity) throws ErrorException {
        try {
            Object comparisonValue = expression.evaluate(world, entity);
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
        } catch (ClassCastException e) {
            throw new ErrorException("Cannot compare " + expression + " to " + entity.getPropertyByName(property).getValue());
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

    @Override
    public SatisfiableDTO getSatisfiableDTO() {
        return new SimpleConditionDTO(property,entityDefinition.getName(), operator.toString(), expression.toString());
    }


    public String getEntityName() {
        return entityDefinition.getName();
    }
    public String getExpressionInString() {
        return expression.toString();
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

    public SimpleConditionDTO getSimpleConditionDTO() {
        return new SimpleConditionDTO(property, entityDefinition.getName(), operator.toString(), expression.toString());
    }
}
