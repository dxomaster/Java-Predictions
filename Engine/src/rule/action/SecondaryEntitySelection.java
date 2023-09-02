package rule.action;

import Exception.ERROR.ErrorException;
import entity.Entity;
import entity.EntityDefinition;
import rule.action.condition.Satisfiable;
import rule.action.condition.SimpleCondition;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class SecondaryEntitySelection {
    int count;
    List<Satisfiable> condition;
    EntityDefinition entityDefinition;

    public SecondaryEntitySelection(int count, List<Satisfiable> condition, EntityDefinition entityDefinition) {
        this.count = count;
        this.condition = condition;
        this.entityDefinition = entityDefinition;
    }
    public List<Entity> selectFromWorld(World world) throws ErrorException {
        int numberToTake = Math.min(count, world.getEntities().get(entityDefinition.getName()).size());


        List<Entity> randomEntities = world.getRandomEntities(entityDefinition.getName(), numberToTake);
        List<Entity> mockList = new ArrayList<>(randomEntities);
        for (Entity randomEntity : mockList) {
            for (Satisfiable simpleCondition : condition) {

                if(!simpleCondition.isSatisfied(world, randomEntity,null))
                    randomEntities.remove(randomEntity);
            }
        }
        return randomEntities;
    }


}
