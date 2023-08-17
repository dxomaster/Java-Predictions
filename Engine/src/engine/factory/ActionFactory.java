package engine.factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.rule.action.Action;
import engine.rule.action.ActionNames;
import engine.rule.action.CalculationOperator;
import engine.world.World;

public class ActionFactory {
    public static Action createAction(World world, PRDAction prdAction) {
        if (world.getEntityDefinitionByName(prdAction.getEntity()) == null)
            throw new RuntimeException("Entity " + prdAction.getEntity() + " not found");
        if (world.getEntityDefinitionByName(prdAction.getEntity()).getPropertyByName(prdAction.getProperty()) == null &&
                world.getEntityDefinitionByName(prdAction.getEntity()).getPropertyByName(prdAction.getResultProp()) == null)
            throw new RuntimeException("Property " + prdAction.getProperty() + " not found");
        switch (prdAction.getType()) {
            case "increase":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.INCREASE, ExpressionFactory.createExpression(world, prdAction));
            case "decrease":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.DECREASE, ExpressionFactory.createExpression(world, prdAction));
            case "set":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.SET, ExpressionFactory.createExpression(world, prdAction));
            case "calculation":
                if (prdAction.getPRDMultiply() != null)
                    return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getResultProp(), CalculationOperator.MULTIPLY, ActionNames.CALCULATION, ExpressionFactory.createExpression(world, prdAction));
                else if (prdAction.getPRDDivide() != null)
                    return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getResultProp(), CalculationOperator.DIVIDE, ActionNames.CALCULATION, ExpressionFactory.createExpression(world, prdAction));
                else
                    throw new RuntimeException("Calculation type not found");
            case "kill":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.KILL);
            default:
                throw new RuntimeException("Action type " + prdAction.getType() + " not found");
        }
    }
}
