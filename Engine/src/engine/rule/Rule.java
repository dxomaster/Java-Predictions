package engine.rule;

import engine.rule.action.Action;
import engine.rule.utils.Activation;

import java.util.List;

public class Rule {
    String name;
    List<Action> actions;
    Activation activation;
}

