package rule.action;

import DTO.ActionableDTO;
import DTO.SatisfiableDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import rule.action.Actionable;
import rule.action.condition.Satisfiable;
import rule.action.expression.Expression;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class Proximity implements Actionable, Satisfiable {
    public String getSourceEntityName() {
        return sourceEntityName;
    }

    public String getTargetEntityName() {
        return targetEntityName;
    }

    public Expression getOf() {
        return of;
    }
    private final List<Actionable> actionsToPreformIfProximityIsSatisfied;

    private final String sourceEntityName;
    private final String targetEntityName;
    private final Expression of;

    public Proximity(String sourceEntityName, String targetEntityName,List<Actionable>actionsToPreformIfProximityIsSatisfied, Expression of) {
        this.sourceEntityName = sourceEntityName;
        this.targetEntityName = targetEntityName;
        this.of = of;
        this.actionsToPreformIfProximityIsSatisfied = actionsToPreformIfProximityIsSatisfied;
    }
    @Override
    public void performAction(World world, Entity entity, int ticks,Entity secondaryEntity) throws WarnException, ErrorException {
        if(isSatisfied(world, entity,secondaryEntity)){
            for(Actionable action : actionsToPreformIfProximityIsSatisfied){
                action.performAction(world, entity, ticks, secondaryEntity);
            }
        }
    }

    @Override
    public SecondaryEntitySelection getSecondaryEntitySelection() {
        return null;
    }

    @Override
    public boolean isSatisfied(World world, Entity entity,Entity secondaryEntity) throws ErrorException {
       return  world.getGrid().isEntityNear(entity, targetEntityName, ((Float)of.evaluate(world, entity,secondaryEntity)).intValue());
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(sourceEntityName);
        return entities;
    }

    @Override
    public SatisfiableDTO getSatisfiableDTO() {
        return null;
    }

    @Override
    public String getName() {
        return "Proximity";
    }

    @Override
    public ActionableDTO getActionableDTO() {
        return null;
    }
}