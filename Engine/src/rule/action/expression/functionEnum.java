package rule.action.expression;

public enum functionEnum implements java.io.Serializable {
    ENVIRONMENT("environment"),
    RANDOM("random");
    public final String functionInString;

    functionEnum(String functionInString) {
        this.functionInString = functionInString;
    }
}
