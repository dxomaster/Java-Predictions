package engine.rule.action;

public enum Actions {
    INCREASE("increase"),
    DECREASE("decrease"),
    SET("set"),
    CALCULATION("calculation"),
    KILL("kill");
    public String actionInString;
    Actions(String actionInString) {
        this.actionInString = actionInString;
    }
}
