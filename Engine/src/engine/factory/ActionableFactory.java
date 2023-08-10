package engine.factory;

import engine.jaxb.schema.generated.PRDAction;
import engine.rule.action.Actionable;
import engine.world.World;

public class ActionableFactory {
    public static Actionable createAction(World world, PRDAction prdAction) {
        if (prdAction.getPRDCondition() != null) {
            return ConditionFactory.createCondition(world, prdAction);
        } else {
            return ActionFactory.createAction(world, prdAction);
        }
    }
}
