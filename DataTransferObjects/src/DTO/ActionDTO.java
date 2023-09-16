package DTO;

public class ActionDTO implements ActionableDTO{
    public String getEntityName() {
        return entityName;
    }

    public String getActionName() {
        return actionName;
    }

    public String[] getExpressions() {
        return expressions;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getOperator() {
        return operator;
    }

    private final String entityName;
    private final String actionName;
    private final String[] expressions;
    private final String propertyName;
    private final String operator;
    private final String secondaryEntityName;

    public ActionDTO(String entityName, String actionName, String[] expressions, String propertyName, String operator, String secondaryEntityName) {
        this.entityName = entityName;
        this.actionName = actionName;
        this.expressions = expressions;
        this.propertyName = propertyName;
        this.operator = operator;
        this.secondaryEntityName = secondaryEntityName;
    }

    @Override
    public String getName() {
        return actionName;
    }

    @Override
    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }
}
