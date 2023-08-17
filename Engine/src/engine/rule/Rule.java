package engine.rule;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.factory.RuleFactory;
import engine.rule.action.Actionable;
import engine.rule.utils.Activation;
import engine.world.World;

import java.util.List;

public class Rule extends RuleFactory implements java.io.Serializable {
    private final String name;
    private final List<Actionable> actions;
    private final Activation activation;

    public Rule(String name, List<Actionable> actions, Activation activation) {
        this.name = name;
        this.actions = actions;
        this.activation = activation;
    }

    @Override
    public String toString() {
        return "Rule: " + name +
                ", Amount of actions: " + actions.size() +
                ", Activation: " + activation;
    }

    public void applyRule(World world, Entity entity, Integer ticks) throws ErrorException {
        if (activation.isActivated(ticks)) {
            for (Actionable action : actions) {
                if (action.getEntities().contains(entity.getName()))
                    try {
                        action.performAction(world, entity);
                    } catch (WarnException ignored) {
                        //these exceptions are OK, continue to next action
                    } catch (ErrorException e) {
                        throw new ErrorException("Error in rule: " + name + " in action: " + action.getName() + ": " + e.getMessage());
                    }

            }
        }

    }


}

