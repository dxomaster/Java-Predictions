package DTO;

import java.util.List;

public class ConditionDTO implements ActionableDTO, SatisfiableDTO{
    List<ActionableDTO> actionsToPerformIfSatisfied;
    List<ActionableDTO> actionsToPerformIfNotSatisfied;
    SimpleConditionDTO simpleCondition;

    public List<ActionableDTO> getActionsToPerformIfSatisfied() {
        return actionsToPerformIfSatisfied;
    }

    public List<ActionableDTO> getActionsToPerformIfNotSatisfied() {
        return actionsToPerformIfNotSatisfied;
    }

    public SimpleConditionDTO getSimpleCondition() {
        return simpleCondition;
    }
    public ConditionDTO(List<ActionableDTO> actionsToPerformIfSatisfied, List<ActionableDTO> actionsToPerformIfNotSatisfied, SimpleConditionDTO simpleCondition) {
        this.actionsToPerformIfSatisfied = actionsToPerformIfSatisfied;
        this.actionsToPerformIfNotSatisfied = actionsToPerformIfNotSatisfied;
        this.simpleCondition = simpleCondition;
    }

    @Override
    public String getName() {
        return "Condition";
    }
}
