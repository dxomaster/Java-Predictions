package engine.factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.rule.action.Actionable;

public class ActionableFactory {
    public static Actionable createAction(PRDAction prdAction) {
        if (prdAction.getPRDCondition() != null) {
            return ConditionFactory.createCondition(prdAction);
        } else {
            return ActionFactory.createAction(prdAction);
        }
    }
}
