package rule.action.condition;

import DTO.SatisfiableDTO;
import DTO.SimpleConditionDTO;
import Exception.ERROR.ErrorException;
import entity.Entity;
import entity.EntityDefinition;
import rule.action.expression.Expression;
import world.World;
import world.utils.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class SimpleCondition implements Satisfiable, java.io.Serializable {
    public Expression getLeftExpression() {
        return leftExpression;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public ConditionOperator getOperator() {
        return operator;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    private final Expression leftExpression;
    private final EntityDefinition entityDefinition;
    private final ConditionOperator operator;
    private final Expression rightExpression;

    public SimpleCondition(EntityDefinition entityDefinition, Expression leftExpression, Expression rightExpression, ConditionOperator operator) {
        this.rightExpression = rightExpression;
        this.leftExpression = leftExpression;
        this.entityDefinition = entityDefinition;
        this.operator = operator;

        PropertyType leftExpressionType = this.leftExpression.getType();
        PropertyType rightExpressionType = this.rightExpression.getType();
        if (leftExpressionType != rightExpressionType) {
            throw new IllegalArgumentException("Left argument " + this.leftExpression + " and right argument " + this.rightExpression + " are not of the same type");
        }
        if (operator == ConditionOperator.LESS_THAN || ConditionOperator.GREATER_THAN == operator) {
            if(leftExpressionType != PropertyType.FLOAT && leftExpressionType != PropertyType.DECIMAL) {
                throw new IllegalArgumentException("Property " + leftExpression + " is not a number");
            }

        }

    }

    @Override
    public boolean isSatisfied(World world, Entity entity) throws ErrorException {
        try {
            Object comparisonValue = rightExpression.evaluate(world, entity);
            Object antecedentValue = leftExpression.evaluate(world, entity);
            PropertyType type = rightExpression.getType();

            if (type == PropertyType.FLOAT) {
                return compareFloat((Float) antecedentValue, (Float) comparisonValue);
            } else if (type == PropertyType.DECIMAL) {
                return compareInteger((Integer) antecedentValue, (Integer) comparisonValue);
            } else if (type == PropertyType.BOOLEAN) {
                return compareBoolean((Boolean) antecedentValue, (Boolean) comparisonValue);
            } else {
                return compareStrings((String) antecedentValue, (String) comparisonValue);
            }
        } catch (ClassCastException e) {
            throw new ErrorException("Cannot compare " + rightExpression + " to " + leftExpression);
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
        return new SimpleConditionDTO(leftExpression.toString(),entityDefinition.getName(), operator.toString(), rightExpression.toString());
    }


    public String getEntityName() {
        return entityDefinition.getName();
    }

    @Override
    public String toString() {
        return "SimpleCondition{" +
                "property='" + leftExpression + '\'' +
                ", entityDefinition=" + entityDefinition +
                ", operator=" + operator +
                ", expression=" + rightExpression +
                '}';
    }

    public SimpleConditionDTO getSimpleConditionDTO() {
        return new SimpleConditionDTO(leftExpression.toString(), entityDefinition.getName(), operator.toString(), rightExpression.toString());
    }
}
