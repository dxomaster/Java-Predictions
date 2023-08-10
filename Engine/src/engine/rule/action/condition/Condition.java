package engine.rule.action.condition;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.rule.action.Actionable;
import engine.world.World;

import java.util.ArrayList;
import java.util.List;

public class Condition implements Satisfiable, Actionable {
    List<Actionable> actionsToPreformIfConditionIsSatisfied;
    List<Actionable> actionsToPreformIfConditionIsNotSatisfied;
    SimpleCondition simpleCondition;

    @Override
    public boolean isSatisfied(World world, Entity entity) throws ErrorException {
        return simpleCondition.isSatisfied(world, entity);
    }

    public Condition(SimpleCondition simpleCondition, List<Actionable> actionsToPreformIfConditionIsSatisfied, List<Actionable> actionsToPreformIfConditionIsNotSatisfied) {
        this.actionsToPreformIfConditionIsSatisfied = actionsToPreformIfConditionIsSatisfied;
        this.actionsToPreformIfConditionIsNotSatisfied = actionsToPreformIfConditionIsNotSatisfied;
        this.simpleCondition = simpleCondition;
    }

    @Override
    public void performAction(World world, Entity entity) throws WarnException, ErrorException {
        if (isSatisfied(world, entity)) {
            for (Actionable action : actionsToPreformIfConditionIsSatisfied) {
                action.performAction(world, entity);
            }
        } else {
            for (Actionable action : actionsToPreformIfConditionIsNotSatisfied) {
                action.performAction(world, entity);
            }
        }
    }

    public String toString() {
        return simpleCondition.toString();
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(simpleCondition.getEntityName());
        return entities;
    }

    @Override
    public String getName() {
        return "condition";
    }


}
