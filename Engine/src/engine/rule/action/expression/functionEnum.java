package engine.rule.action.expression;

public enum functionEnum {
    ENVIRONMENT("environment"),
    RANDOM("random");
    public final String functionInString;

    functionEnum(String functionInString) {
        this.functionInString = functionInString;
    }
}
