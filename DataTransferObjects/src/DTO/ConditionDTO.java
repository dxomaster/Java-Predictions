package DTO;

import java.util.List;

public class ConditionDTO implements ActionableDTO, SatisfiableDTO{
    List<ActionableDTO> actionsToPerformIfSatisfied;
    List<ActionableDTO> actionsToPerformIfNotSatisfied;
    SimpleConditionDTO simpleCondition;
    private final String secondaryEntityName;

    public List<ActionableDTO> getActionsToPerformIfSatisfied() {
        return actionsToPerformIfSatisfied;
    }

    public List<ActionableDTO> getActionsToPerformIfNotSatisfied() {
        return actionsToPerformIfNotSatisfied;
    }

    public SimpleConditionDTO getSimpleCondition() {
        return simpleCondition;
    }
    public ConditionDTO(List<ActionableDTO> actionsToPerformIfSatisfied, List<ActionableDTO> actionsToPerformIfNotSatisfied, SimpleConditionDTO simpleCondition, String secondaryEntityName) {
        this.actionsToPerformIfSatisfied = actionsToPerformIfSatisfied;
        this.actionsToPerformIfNotSatisfied = actionsToPerformIfNotSatisfied;
        this.simpleCondition = simpleCondition;
        this.secondaryEntityName = secondaryEntityName;
    }

    @Override
    public String getName() {
        return "Condition";
    }

    @Override
    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }
}
