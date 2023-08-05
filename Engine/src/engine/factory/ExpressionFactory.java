package engine.factory;

import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDCondition;
import engine.jaxb.schema.generated.PRDMultiply;
import engine.rule.action.expression.*;
import engine.world.World;

import java.util.ArrayList;
import java.util.List;

public class ExpressionFactory {
    public static Expression createExpression(PRDCondition prdCondition)
    {
        return createExpression(prdCondition.getEntity(), prdCondition.getValue());
    }
    public static Expression[] createExpression(PRDAction prdAction) {
        List<Expression> expressions = new ArrayList<>();
        switch (prdAction.getType()) {
            case "increase":
            case "decrease":
                expressions.add(createExpression(prdAction.getEntity(), prdAction.getBy()));
                break;
            case "set":
                expressions.add(createExpression(prdAction.getEntity(), prdAction.getValue()));
                break;
            case "calculation":
                return createExpressionArray(prdAction);
            default:
                throw new RuntimeException("Expression type " + prdAction.getType() + " not found");
        }
        return expressions.toArray(new Expression[0]);
    }

    public static Expression[] createExpressionArray(PRDAction prdAction) {
        Expression[] expressions = new Expression[2];
        if (prdAction.getPRDMultiply() != null) {
            expressions[0] = createExpression(prdAction.getEntity(), prdAction.getPRDMultiply().getArg1());
            expressions[1] = createExpression(prdAction.getEntity(), prdAction.getPRDMultiply().getArg2());
        } else if (prdAction.getPRDDivide() != null) {
            expressions[0] = createExpression(prdAction.getEntity(), prdAction.getPRDDivide().getArg1());
            expressions[1] = createExpression(prdAction.getEntity(), prdAction.getPRDDivide().getArg2());
        }
        else {
            throw new RuntimeException("Calculation type " + prdAction.getType() + " not found");
        }
        return expressions;
    }

    public static Expression createExpression(String entityName, String expression) {
        try {
            return createfunctionExpression(expression);
        } catch (RuntimeException e) {
            try {
                return createPropertyExpression(entityName, expression);
            } catch (RuntimeException e1) {
                try {
                    return createValueExpression(expression);
                } catch (RuntimeException e2) {
                    throw new RuntimeException("Expression " + expression + " is invalid");
                }
            }
        }
    }

    public static Expression createfunctionExpression(String expression) {
        String functionName = getFunctionName(expression);
        Object[] arguments = getArguments(expression);
        return new FunctionExpression(functionName, arguments);
    }

    public static Expression createPropertyExpression(String entityName, String propertyName) {
        EntityDefinition entity = World.getEntityDefinitionByName(entityName);
        if (entity == null) {
            throw new RuntimeException("Entity " + entityName + " not found");
        }
        return new PropertyExpression(entity, propertyName);
    }

    public static Expression createValueExpression(Object value) {
        return new ValueExpression(value);
    }

    private static String getFunctionName(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }

    private static Object[] getArguments(String expression) {
        String[] arguments = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")")).split(",");
        Object[] argumentsObject = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentsObject[i] = arguments[i];
        }
        return argumentsObject;
    }
}
