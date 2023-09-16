package DTO;

import java.util.List;

public class MultipleConditionDTO implements ActionableDTO, SatisfiableDTO{
    List<SatisfiableDTO> conditions;
    String operator;
    List<ActionableDTO> actionsToPerformIfSatisfied;
    List<ActionableDTO> actionsToPerformIfNotSatisfied;
    private final String secondaryEntityName;

    public List<SatisfiableDTO> getConditions() {
        return conditions;
    }

    public String getOperator() {
        return operator;
    }

    public List<ActionableDTO> getActionsToPerformIfSatisfied() {
        return actionsToPerformIfSatisfied;
    }

    public List<ActionableDTO> getActionsToPerformIfNotSatisfied() {
        return actionsToPerformIfNotSatisfied;
    }
    public MultipleConditionDTO(List<SatisfiableDTO> conditions, String operator, List<ActionableDTO> actionsToPerformIfSatisfied, List<ActionableDTO> actionsToPerformIfNotSatisfied, String secondaryEntityName) {
        this.conditions = conditions;
        this.operator = operator;
        this.actionsToPerformIfSatisfied = actionsToPerformIfSatisfied;
        this.actionsToPerformIfNotSatisfied = actionsToPerformIfNotSatisfied;
        this.secondaryEntityName = secondaryEntityName;
    }

    @Override
    public String getName() {
        return "Multiple Condition";
    }

    @Override
    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }
}
