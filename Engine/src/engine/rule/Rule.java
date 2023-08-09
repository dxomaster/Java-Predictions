package engine.rule;

import Exception.WARN.WarnException;
import engine.entity.Entity;
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
    public String getEntityName() {
        return name;
    }
    public List<Actionable> getActions() {
        return actions;
    }
    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", actions=" + actions +
                ", activation=" + activation +
                '}';
    }
    public void applyRule(Entity entity, Integer ticks) throws WarnException {
        if (activation.isActivated(ticks)){
            for (Actionable action : actions) {
                action.performAction(entity);
            }
        }

    }

    public String getName() {
        return name;
    }

    public boolean checkActivation(Integer ticks) {
        return activation.isActivated(ticks);
    }
}

