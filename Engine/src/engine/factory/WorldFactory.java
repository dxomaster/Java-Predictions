package engine.factory;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.*;
import engine.world.World;
import engine.world.utils.Property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldFactory {
     public static World createWorld(PRDWorld prdWorld) {
         Map<String,List<Entity>> entityList = new HashMap<>();
         for (PRDEntity prdEntity : prdWorld.getPRDEntities().getPRDEntity()) {
             List<Entity> entityList = EntityFactory.createEntityList(prdEntity);
             entityList.put(prdEntity.getName(),entityList);
         }
         List<EntityDefinition> entityDefinitions = EntityFactory.createEntityDefinitionList(prdWorld.getPRDEntities().getPRDEntity());
         List<Property> environmentProperties = PropertyFactory.createPropertyList(prdWorld.getPRDEvironment().getPRDEnvProperty());
        return new World(environmentProperties,entityList,entityDefinitions);
     }
}
