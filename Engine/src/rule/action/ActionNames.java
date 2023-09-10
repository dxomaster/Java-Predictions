package rule.action;

public enum ActionNames implements java.io.Serializable {
    INCREASE("increase"),
    DECREASE("decrease"),
    SET("set"),
    CALCULATION("calculation"),
    KILL("kill"),
    REPLACE("replace"),
    PROXIMITY("proximity");

    public final String actionInString;

    ActionNames(String actionInString) {
        this.actionInString = actionInString;
    }

}
