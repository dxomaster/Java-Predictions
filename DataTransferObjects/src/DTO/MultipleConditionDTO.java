package DTO;

import java.util.List;

public class MultipleConditionDTO implements ActionableDTO, SatisfiableDTO{
    List<SatisfiableDTO> conditions;
    String operator;
    List<ActionableDTO> actionsToPerformIfSatisfied;
    List<ActionableDTO> actionsToPerformIfNotSatisfied;

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
    public MultipleConditionDTO(List<SatisfiableDTO> conditions, String operator, List<ActionableDTO> actionsToPerformIfSatisfied, List<ActionableDTO> actionsToPerformIfNotSatisfied) {
        this.conditions = conditions;
        this.operator = operator;
        this.actionsToPerformIfSatisfied = actionsToPerformIfSatisfied;
        this.actionsToPerformIfNotSatisfied = actionsToPerformIfNotSatisfied;
    }

    @Override
    public String getName() {
        return "Multiple Condition";
    }
}
