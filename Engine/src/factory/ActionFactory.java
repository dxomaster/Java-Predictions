package factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDCondition;
import entity.EntityDefinition;
import rule.action.*;
import rule.action.condition.Satisfiable;
import rule.action.expression.Expression;
import world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActionFactory {
    public static Actionable createAction(World world, PRDAction prdAction) {
        boolean isEntityAction = !prdAction.getType().equals("proximity") && !prdAction.getType().equals("replace");
        if (world.getEntityDefinitionByName(prdAction.getEntity()) == null && isEntityAction)
            throw new RuntimeException("Entity " + prdAction.getEntity() + " not found");
        if (prdAction.getResultProp() != null || prdAction.getProperty() != null)
            if (world.getEntityDefinitionByName(prdAction.getEntity()).getPropertyByName(prdAction.getProperty()) == null &&
                    world.getEntityDefinitionByName(prdAction.getEntity()).getPropertyByName(prdAction.getResultProp()) == null)
                throw new RuntimeException("Property " + prdAction.getProperty() + " not found");
        SecondaryEntitySelection secondaryEntitySelection = null;
        if (prdAction.getPRDSecondaryEntity() != null) {
            secondaryEntitySelection = ActionFactory.createSecondaryEntity(world, prdAction.getPRDSecondaryEntity());
        }
        switch (prdAction.getType()) {
            case "increase":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.INCREASE, secondaryEntitySelection, ExpressionFactory.createExpression(world, prdAction));
            case "decrease":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.DECREASE, secondaryEntitySelection, ExpressionFactory.createExpression(world, prdAction));
            case "set":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.SET, secondaryEntitySelection, ExpressionFactory.createExpression(world, prdAction));
            case "calculation":
                if (prdAction.getPRDMultiply() != null)
                    return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getResultProp(), CalculationOperator.MULTIPLY, ActionNames.CALCULATION, secondaryEntitySelection, ExpressionFactory.createExpression(world, prdAction));
                else if (prdAction.getPRDDivide() != null)
                    return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getResultProp(), CalculationOperator.DIVIDE, ActionNames.CALCULATION, secondaryEntitySelection, ExpressionFactory.createExpression(world, prdAction));
                else
                    throw new RuntimeException("Calculation type not found");
            case "kill":
                return new Action(world.getEntityDefinitionByName(prdAction.getEntity()), prdAction.getProperty(), ActionNames.KILL, secondaryEntitySelection);
            case "proximity":
                PRDAction.PRDBetween prdBetween = prdAction.getPRDBetween();
                if (world.getEntityDefinitionByName(prdBetween.getSourceEntity()) == null || world.getEntityDefinitionByName(prdBetween.getTargetEntity()) == null)
                    throw new RuntimeException("Entity " + prdBetween.getSourceEntity() + " not found");
                Expression[] exp = ExpressionFactory.createExpression(world, prdAction);
                List<Actionable> actions = new ArrayList<>();
                for (PRDAction action : prdAction.getPRDActions().getPRDAction()) {
                    actions.add(createAction(world, action));
                }
                return new Proximity(prdBetween.getSourceEntity(), prdBetween.getTargetEntity(), actions, exp[0]);
            case "replace":
                if (world.getEntityDefinitionByName(prdAction.getKill()) == null)
                    throw new RuntimeException("Entity " + prdAction.getKill() + " not found");
                if (world.getEntityDefinitionByName(prdAction.getCreate()) == null)
                    throw new RuntimeException("Entity " + prdAction.getCreate() + " not found");
                try {
                    return new Replace(prdAction.getKill(), prdAction.getCreate(), prdAction.getMode());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            default:
                throw new RuntimeException("Action type " + prdAction.getType() + " not found");
        }

    }

    public static SecondaryEntitySelection createSecondaryEntity(World world, PRDAction.PRDSecondaryEntity secondary) {
        EntityDefinition definition = world.getEntityDefinitionByName(secondary.getEntity());
        int count = Integer.parseInt(secondary.getPRDSelection().getCount());
        List<Satisfiable> conditions = new ArrayList<>();
        if (Objects.equals(secondary.getPRDSelection().getPRDCondition().getSingularity(), "single")) {
            conditions.add(ConditionFactory.createCondition(world, secondary.getPRDSelection().getPRDCondition()));
        }
        else if (Objects.equals(secondary.getPRDSelection().getPRDCondition().getSingularity(), "multiple")) {
            for (PRDCondition condition : secondary.getPRDSelection().getPRDCondition().getPRDCondition()) {
                conditions.add(ConditionFactory.createCondition(world, condition));
            }

        }
        else
            throw new RuntimeException("Singularity " + secondary.getPRDSelection().getPRDCondition().getSingularity() + " not found");
        return new SecondaryEntitySelection(count, conditions, definition);
    }
}
