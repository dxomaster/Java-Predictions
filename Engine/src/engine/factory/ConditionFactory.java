package engine.factory;

import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDCondition;
import engine.rule.action.Actionable;
import engine.rule.action.condition.*;
import engine.world.World;

import java.util.ArrayList;
import java.util.List;

public class ConditionFactory {
    public static Actionable createCondition(PRDAction prdAction) {
        PRDCondition prdCondition = prdAction.getPRDCondition();
        switch (prdCondition.getSingularity()) {
            case "single":
                return createSingleCondition(prdAction, createSimpleCondition(prdCondition));
            case "multiple":
                return createMultipleCondition(prdAction);
            default:
                throw new IllegalArgumentException("Invalid singularity: " + prdCondition.getSingularity());
        }
    }

    private static MultipleCondition createMultipleCondition(PRDAction prdAction) {
        List<Actionable> thenActions = new ArrayList<>();
        List<Actionable> elseActions = new ArrayList<>();
        if (prdAction.getPRDThen() != null) {
            for (PRDAction action : prdAction.getPRDThen().getPRDAction()) {
                thenActions.add(ActionableFactory.createAction(action));
            }
        }
        if (prdAction.getPRDElse() != null) {
            for (PRDAction action : prdAction.getPRDElse().getPRDAction()) {
                elseActions.add(ActionFactory.createAction(action));
            }
        }
        List<Satisfiable> conditions = new ArrayList<>();

        for (PRDCondition condition : prdAction.getPRDCondition().getPRDCondition()) {
            if (condition.getSingularity().equals("single"))
                conditions.add(createSimpleCondition(condition));
            else
                conditions.add(createMultipleCondition(condition));
        }


        switch (prdAction.getPRDCondition().getLogical()) {
            case "and":
                return new MultipleCondition(LogicalOperator.AND, conditions, thenActions, elseActions);
            case "or":
                return new MultipleCondition(LogicalOperator.OR, conditions, thenActions, elseActions);
            default:
                throw new IllegalArgumentException("Invalid logical operator: " + prdAction.getPRDCondition().getLogical());
        }

    }

    private static MultipleCondition createMultipleCondition(PRDCondition prdCondition) {
        List<Satisfiable> conditions = new ArrayList<>();

        for (PRDCondition condition : prdCondition.getPRDCondition()) {
            if (condition.getSingularity().equals("single"))
                conditions.add(createSimpleCondition(condition));
            else
                conditions.add(createMultipleCondition(condition));
        }
        switch (prdCondition.getLogical()) {
            case "and":
                return new MultipleCondition(LogicalOperator.AND, conditions, null, null);
            case "or":
                return new MultipleCondition(LogicalOperator.OR, conditions, null, null);
            default:
                throw new IllegalArgumentException("Invalid logical operator: " + prdCondition.getLogical());
        }

    }

    private static SimpleCondition createSimpleCondition(PRDCondition prdCondition) {
        EntityDefinition entityDefinition = World.getEntityDefinitionByName(prdCondition.getEntity());
        if (entityDefinition == null)
            throw new RuntimeException("Entity " + prdCondition.getEntity() + " not found");
        if (entityDefinition.getPropertyByName(prdCondition.getProperty()) == null)
            throw new RuntimeException("Property " + prdCondition.getProperty() + " not found");
        switch (prdCondition.getOperator()) {
            case "bt":
                return new SimpleCondition(World.getEntityDefinitionByName(prdCondition.getEntity()), ExpressionFactory.createExpression(prdCondition), prdCondition.getProperty(), ConditionOperator.GREATER_THAN);
            case "lt":
                return new SimpleCondition(World.getEntityDefinitionByName(prdCondition.getEntity()), ExpressionFactory.createExpression(prdCondition), prdCondition.getProperty(), ConditionOperator.LESS_THAN);
            case "=":
                return new SimpleCondition(World.getEntityDefinitionByName(prdCondition.getEntity()), ExpressionFactory.createExpression(prdCondition), prdCondition.getProperty(), ConditionOperator.EQUALS);
            case "!=":
                return new SimpleCondition(World.getEntityDefinitionByName(prdCondition.getEntity()), ExpressionFactory.createExpression(prdCondition), prdCondition.getProperty(), ConditionOperator.NOT_EQUALS);
            default:
                throw new IllegalArgumentException("Invalid condition operator: " + prdCondition.getOperator());
        }
    }

    private static Condition createSingleCondition(PRDAction prdAction, SimpleCondition simpleCondition) {

        List<Actionable> thenActions = new ArrayList<>();
        List<Actionable> elseActions = new ArrayList<>();
        if (prdAction.getPRDThen() != null) {
            for (PRDAction action : prdAction.getPRDThen().getPRDAction()) {
                thenActions.add(ActionableFactory.createAction(action));
            }
        }
        if (prdAction.getPRDElse() != null) {
            for (PRDAction action : prdAction.getPRDElse().getPRDAction()) {
                elseActions.add(ActionFactory.createAction(action));
            }
        }
        return new Condition(simpleCondition, thenActions, elseActions);
    }
}
