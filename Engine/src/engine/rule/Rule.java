package engine.rule;

import engine.rule.action.Action;
import engine.rule.action.Actionable;
import engine.rule.utils.Activation;

import java.util.List;

public class Rule {
    String name;
    List<Actionable> actions;
    Activation activation;
    public Rule(String name, List<Actionable> actions, Activation activation) {
        this.name = name;
        this.actions = actions;
        this.activation = activation;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", actions=" + actions +
                ", activation=" + activation +
                '}';
    }
}

