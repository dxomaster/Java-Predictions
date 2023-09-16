package rule.action.condition;

import DTO.ActionableDTO;
import DTO.MultipleConditionDTO;
import DTO.SatisfiableDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import rule.action.Actionable;
import rule.action.SecondaryEntitySelection;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition implements Satisfiable, Actionable, java.io.Serializable {
    private final SecondaryEntitySelection secondaryEntitySelection;
    private final List<Actionable> actionsToPreformIfConditionIsSatisfied;
    private final List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    private final LogicalOperator operator;
    private final List<Satisfiable> conditions;

    public MultipleCondition(LogicalOperator operator, List<Satisfiable> conditions, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied, SecondaryEntitySelection secondaryEntitySelection) {
        this.operator = operator;
        this.conditions = conditions;
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
        this.secondaryEntitySelection = secondaryEntitySelection;
    }

    @Override
    public boolean isSatisfied(World world, Entity entity,Entity secondaryEntity) throws ErrorException {
        switch (operator) {
            case AND:
                for (Satisfiable condition : conditions) {
                    if (!condition.isSatisfied(world, entity,secondaryEntity)) {
                        return false;
                    }
                }
                return true;
            case OR:
                for (Satisfiable condition : conditions) {
                    if (condition.isSatisfied(world, entity,secondaryEntity)) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public void performAction(World world, Entity entity, int ticks,Entity secondaryEntity) throws WarnException, ErrorException {
        if (isSatisfied(world, entity,secondaryEntity)) {
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(world, entity, ticks,secondaryEntity);
            }
        } else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(world, entity, ticks,secondaryEntity);
            }
        }
    }

    @Override
    public SecondaryEntitySelection getSecondaryEntitySelection() {
        return secondaryEntitySelection;
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        for (Satisfiable condition : conditions) {
            entities.addAll(condition.getEntities());
        }
        return entities;
    }

    @Override
    public SatisfiableDTO getSatisfiableDTO() {
        return null;
    }

    @Override
    public String getName() {
        return "multiple condition";
    }

    @Override
    public ActionableDTO getActionableDTO() {
        List<SatisfiableDTO> conditions = new ArrayList<>();
        for (Satisfiable condition : this.conditions) {
            conditions.add(condition.getSatisfiableDTO());
        }
        List<ActionableDTO> actionsToPreformIfConditionIsSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsSatisfied) {
            actionsToPreformIfConditionIsSatisfied.add(action.getActionableDTO());
        }
        List<ActionableDTO> actionsToPreformIfConditionIsNotSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsNotSatisfied) {
            actionsToPreformIfConditionIsNotSatisfied.add(action.getActionableDTO());
        }
        String secondaryEntityName = "none";
        if (secondaryEntitySelection != null) {
            secondaryEntityName = secondaryEntitySelection.getSecondaryEntityName();
        }
        return new MultipleConditionDTO(conditions, this.operator.toString(), actionsToPreformIfConditionIsSatisfied, actionsToPreformIfConditionIsNotSatisfied, secondaryEntityName);
    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "actionsToPreformIfConditionIsSatisfied=" + actionsToPreformIfConditionIsSatisfied +
                ", actionsToPreformIfConditionIsNotSatisfied=" + actionsToPreformIfConditionIsNotSatisfied +
                ", operator=" + operator +
                ", conditions=" + conditions +
                '}';
    }
}
