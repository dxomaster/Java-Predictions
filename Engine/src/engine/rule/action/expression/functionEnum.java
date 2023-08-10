package engine.rule.action.expression;

public enum functionEnum {
    ENVIRONMENT("enviroment"),
    RANDOM("random");
    public String functionInString;

    functionEnum(String functionInString) {
        this.functionInString = functionInString;
    }
}
