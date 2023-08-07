package engine.factory;

import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.*;
import engine.rule.Rule;
import engine.world.World;
import engine.world.utils.Property;

import java.util.List;

public class WorldFactory {
    public static World defineWorld(PRDWorld prdWorld)
    {
        List<EntityDefinition> entityDefinitions = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
        World.setEntityDefinitionList(entityDefinitions);
        List<Property> environmentProperties = PropertyFactory.createPropertyList(prdWorld.getPRDEvironment().getPRDEnvProperty());
        World.setEnvironmentVariables(environmentProperties);
        List<Rule> ruleList = RuleFactory.createRuleList(prdWorld.getPRDRules().getPRDRule());
        List<Object> termination = prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond();
        Integer ticks = null, seconds = null;
        for (Object object : termination) {
            if (object instanceof PRDByTicks) {
                ticks = ((PRDByTicks) object).getCount();
            }
            else if (object instanceof PRDBySecond) {
                seconds = ((PRDBySecond) object).getCount();
            }
        }
        return new World(environmentProperties,ruleList,ticks,seconds,entityDefinitions);

    }
}
