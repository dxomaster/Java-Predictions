package DTO;

public class SimpleConditionDTO implements SatisfiableDTO{
    private final String property;
    private final String entityName;
    private final String operator;
    private final String expression;

    public SimpleConditionDTO(String property, String entityName, String operator, String expression) {
        this.property = property;
        this.entityName = entityName;
        this.operator = operator;
        this.expression = expression;
    }

    public String getProperty() {
        return property;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getOperator() {
        return operator;
    }

    public String getExpression() {
        return expression;
    }
}
