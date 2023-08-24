package factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDActions;
import engine.jaxb.schema.generated.PRDRule;
import rule.Rule;
import rule.action.Actionable;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class RuleFactory {
    public static Rule createRule(World world, PRDRule prdRule) {
        List<Actionable> actions = createActions(world, prdRule.getPRDActions());
        return new Rule(prdRule.getName(), actions, ActivationFactory.createActivation(prdRule.getPRDActivation()));
    }

    private static List<Actionable> createActions(World world, PRDActions prdActions) {
        List<Actionable> actions = new ArrayList<>();
        for (PRDAction prdAction : prdActions.getPRDAction()) {
            actions.add(ActionableFactory.createAction(world, prdAction));
        }
        return actions;
    }

    public static List<Rule> createRuleList(World world, List<PRDRule> prdRule) {
        List<Rule> rules = new ArrayList<>();
        for (PRDRule rule : prdRule)
            rules.add(createRule(world, rule));
        return rules;
    }
}
