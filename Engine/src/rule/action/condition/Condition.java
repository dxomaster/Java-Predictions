package rule.action.condition;

import DTO.ActionableDTO;
import DTO.ConditionDTO;
import DTO.SatisfiableDTO;
import DTO.SimpleConditionDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import rule.action.Actionable;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class Condition implements Satisfiable, Actionable, java.io.Serializable {
    private final List<Actionable> actionsToPreformIfConditionIsSatisfied;
    private final List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    private final SimpleCondition simpleCondition;

    public Condition(SimpleCondition simpleCondition, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied) {
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
        this.simpleCondition = simpleCondition;
    }

    @Override
    public boolean isSatisfied(World world, Entity entity) throws ErrorException {
        return simpleCondition.isSatisfied(world, entity);
    }

    @Override
    public void performAction(World world, Entity entity, int ticks) throws WarnException, ErrorException {
        if (isSatisfied(world, entity)) {
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(world, entity, ticks);
            }
        } else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(world, entity, ticks);
            }
        }
    }

    public String toString() {
        return simpleCondition.toString();
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(simpleCondition.getEntityName());
        return entities;
    }

    @Override
    public SatisfiableDTO getSatisfiableDTO() {
        List<ActionableDTO> actionsToPreformIfConditionIsSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsSatisfied) {
            actionsToPreformIfConditionIsSatisfied.add(action.getActionableDTO());
        }
        List<ActionableDTO> actionsToPreformIfConditionIsNotSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsNotSatisfied) {
            actionsToPreformIfConditionIsNotSatisfied.add(action.getActionableDTO());
        }
        return new ConditionDTO(actionsToPreformIfConditionIsSatisfied, actionsToPreformIfConditionIsNotSatisfied, simpleCondition.getSimpleConditionDTO());
    }

    @Override
    public String getName() {
        return "condition";
    }

    @Override
    public ActionableDTO getActionableDTO() {
        List<ActionableDTO> actionsToPreformIfConditionIsSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsSatisfied) {
            actionsToPreformIfConditionIsSatisfied.add(action.getActionableDTO());
        }
        List<ActionableDTO> actionsToPreformIfConditionIsNotSatisfied = new ArrayList<>();
        for (Actionable action : this.actionsToPreformIfConditionIsNotSatisfied) {
            actionsToPreformIfConditionIsNotSatisfied.add(action.getActionableDTO());
        }
        SimpleConditionDTO simpleConditionDTO = new SimpleConditionDTO(simpleCondition.getProperty(), simpleCondition.getEntityName(), simpleCondition.getOperator().toString(), simpleCondition.getExpressionInString());
        return new ConditionDTO(actionsToPreformIfConditionIsSatisfied, actionsToPreformIfConditionIsNotSatisfied, simpleConditionDTO);
    }


}
