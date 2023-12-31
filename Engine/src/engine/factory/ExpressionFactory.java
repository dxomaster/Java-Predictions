package engine.factory;

import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDCondition;
import engine.rule.action.expression.Expression;
import engine.rule.action.expression.FunctionExpression;
import engine.rule.action.expression.PropertyExpression;
import engine.rule.action.expression.ValueExpression;
import engine.world.World;
import engine.world.utils.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class ExpressionFactory {
    public static Expression createExpression(World world, PRDCondition prdCondition) {

        return createExpression(world, prdCondition.getEntity(), prdCondition.getProperty(), prdCondition.getValue());
    }

    private static PropertyType getTypeByPropertyName(World world, String propertyName, String entityName) {
        return world.getEntityDefinitionByName(entityName).getPropertyByName(propertyName).getType();
    }

    public static Expression[] createExpression(World world, PRDAction prdAction) {
        List<Expression> expressions = new ArrayList<>();
        switch (prdAction.getType()) {
            case "increase":
            case "decrease":
                expressions.add(createExpression(world, prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy()));
                break;
            case "set":
                expressions.add(createExpression(world, prdAction.getEntity(), prdAction.getProperty(), prdAction.getValue()));
                break;
            case "calculation":
                return createExpressionArray(world, prdAction);
            default:
                throw new RuntimeException("Expression type " + prdAction.getType() + " not found");
        }
        return expressions.toArray(new Expression[0]);
    }

    public static Expression[] createExpressionArray(World world, PRDAction prdAction) {
        Expression[] expressions = new Expression[2];
        if (prdAction.getPRDMultiply() != null) {
            expressions[0] = createExpression(world, prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDMultiply().getArg1());
            expressions[1] = createExpression(world, prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDMultiply().getArg2());
        } else if (prdAction.getPRDDivide() != null) {
            expressions[0] = createExpression(world, prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDDivide().getArg1());
            expressions[1] = createExpression(world, prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDDivide().getArg2());
        } else {
            throw new RuntimeException("Calculation type " + prdAction.getType() + " not found");
        }
        return expressions;
    }

    public static Expression createExpression(World world, String entityName, String PropertyName, String expression) {
        PropertyType type = getTypeByPropertyName(world, PropertyName, entityName);
        try {
            return createfunctionExpression(world, expression);
        } catch (RuntimeException e) {
            try {
                return createPropertyExpression(world, entityName, expression);
            } catch (RuntimeException e1) {
                try {
                    return createValueExpression(world, entityName, expression, type);
                } catch (RuntimeException e2) {
                    throw new RuntimeException("Expression " + expression + " is not a valid property, function or value expression");
                }
            }
        }
    }

    public static Expression createfunctionExpression(World world, String expression) {
        String functionName = getFunctionName(expression);
        Object[] arguments = getArguments(expression);
        return new FunctionExpression(world, functionName, arguments);
    }

    public static Expression createPropertyExpression(World world, String entityName, String propertyName) {
        EntityDefinition entity = world.getEntityDefinitionByName(entityName);
        if (entity == null) {
            throw new RuntimeException("Entity " + entityName + " not found");
        }
        if (entity.getPropertyByName(propertyName) == null) {
            throw new RuntimeException("Property " + propertyName + " not found");
        }
        return new PropertyExpression(entity, propertyName);
    }

    public static Expression createValueExpression(World world, String entityName, Object value, PropertyType type) {
        EntityDefinition entity = world.getEntityDefinitionByName(entityName);
        if (entity == null) {
            throw new RuntimeException("Entity " + entityName + " not found");
        }
        switch (type) {
            case BOOLEAN:
                value = Boolean.parseBoolean((String) value);
                break;
            case FLOAT:
                value = Float.parseFloat((String) value);
                break;
            case DECIMAL:
                value = Integer.parseInt((String) value);
                break;
            case STRING:
                break;
        }
        return new ValueExpression(value, type);
    }

    private static String getFunctionName(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }

    private static Object[] getArguments(String expression) {
        String[] arguments = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")")).split(",");
        Object[] argumentsObject = new Object[arguments.length];
        System.arraycopy(arguments, 0, argumentsObject, 0, arguments.length);
        return argumentsObject;
    }
}
