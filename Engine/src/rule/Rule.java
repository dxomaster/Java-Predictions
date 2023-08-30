package rule;

import DTO.ActionableDTO;
import DTO.RuleDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import factory.RuleFactory;
import rule.action.Actionable;
import rule.utils.Activation;
import world.World;

import java.util.ArrayList;
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
                        action.performAction(world, entity,ticks);
                    } catch (WarnException ignored) {
                        //these exceptions are OK, continue to next action
                    } catch (ErrorException e) {
                        throw new ErrorException("Error in rule: " + name + " in action: " + action.getName() + ": " + e.getMessage());
                    }

            }
        }

    }


    public RuleDTO getRuleDTO() {
        List<ActionableDTO> actions = new ArrayList<>();
        for (Actionable action : this.actions) {
            actions.add(action.getActionableDTO());
        }

        return new RuleDTO(name,this.activation.getProbability().floatValue(),this.activation.getTicks().floatValue(),
                actions.size(), actions);
    }
}

