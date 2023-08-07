package engine.factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.jaxb.schema.generated.PRDActions;
import engine.jaxb.schema.generated.PRDRule;
import engine.rule.Rule;
import engine.rule.action.Actionable;

import java.util.ArrayList;
import java.util.List;

public class RuleFactory {
    public static Rule createRule(PRDRule prdRule){
        List<Actionable> actions = createActions(prdRule.getPRDActions());
        return new Rule(prdRule.getName(), actions, ActivationFactory.createActivation(prdRule.getPRDActivation()));
    }
    private static List<Actionable> createActions(PRDActions prdActions) {
        List<Actionable> actions = new ArrayList<>();
        for (PRDAction prdAction : prdActions.getPRDAction()) {
            actions.add(ActionableFactory.createAction(prdAction));
        }
        return actions;
    }

    public static List<Rule> createRuleList(List<PRDRule> prdRule) {

        List<Rule> rules = new ArrayList<>();
        for (PRDRule rule : prdRule) {
            rules.add(createRule(rule));
        }
        return rules;
    }
}
