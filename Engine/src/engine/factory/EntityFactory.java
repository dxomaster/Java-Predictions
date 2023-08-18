package engine.factory;

import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDEntity;
import engine.world.utils.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityFactory {
    public static Entity createEntity(EntityDefinition entityDefinition) throws WarnException {
        Map<String, Property> entityProperties = PropertyFactory.createPropertyList(entityDefinition);
        return new Entity(entityDefinition.getName(), entityProperties);
    }

    public static EntityDefinition createEntityDefinition(PRDEntity prdEntity) throws WarnException {

        Map<String, Property> entityProperties = PropertyFactory.createPropertyList(prdEntity);
        return new EntityDefinition(prdEntity.getName(), entityProperties, prdEntity.getPRDPopulation());
    }

    public static Map<String, EntityDefinition> createEntityDefinitionList(List<PRDEntity> prdEntity) throws WarnException {
        Map<String, EntityDefinition> entityDefinitions = new HashMap<>();
        for (PRDEntity entity : prdEntity) {
            entityDefinitions.put(entity.getName(), createEntityDefinition(entity));
        }
        return entityDefinitions;

    }

    public static List<Entity> createEntityList(EntityDefinition entityDefinition) throws WarnException {
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < entityDefinition.getPopulation(); i++) {
            entities.add(createEntity(entityDefinition));
        }
        return entities;
    }
}
