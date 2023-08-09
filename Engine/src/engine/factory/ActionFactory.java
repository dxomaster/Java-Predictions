package engine.factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.rule.action.Action;
import engine.rule.action.ActionNames;
import engine.rule.action.CalculationOperator;
import engine.world.World;
public class ActionFactory {
    public static Action createAction(PRDAction prdAction) {
        if (World.getEntityDefinitionByName(prdAction.getEntity()) == null)
            throw new RuntimeException("Entity " + prdAction.getEntity() + " not found");
        switch (prdAction.getType()) {
            case "increase":
                return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getProperty(), ActionNames.INCREASE,ExpressionFactory.createExpression(prdAction));
            case "decrease":
                return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getProperty(), ActionNames.DECREASE,ExpressionFactory.createExpression(prdAction));
            case "set":
                return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getProperty(), ActionNames.SET,ExpressionFactory.createExpression(prdAction));
            case "calculation":
                if (prdAction.getPRDMultiply() != null)
                    return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getResultProp(),CalculationOperator.MULTIPLY, ActionNames.CALCULATION,ExpressionFactory.createExpression(prdAction));
                else if (prdAction.getPRDDivide() != null)
                    return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getResultProp(),CalculationOperator.DIVIDE, ActionNames.CALCULATION,ExpressionFactory.createExpression(prdAction));
                else
                    throw new RuntimeException("Calculation type not found");
            case "kill":
                return new Action(World.getEntityDefinitionByName(prdAction.getEntity()),prdAction.getProperty(), ActionNames.KILL);
            default:
                throw new RuntimeException("Action type " + prdAction.getType() + " not found");
        }
    }
}
