package engine.rule.action;

public enum ActionNames {
    INCREASE("increase"),
    DECREASE("decrease"),
    SET("set"),
    CALCULATION("calculation"),
    KILL("kill");
    public final String actionInString;
    ActionNames(String actionInString) {
        this.actionInString = actionInString;
    }

}
