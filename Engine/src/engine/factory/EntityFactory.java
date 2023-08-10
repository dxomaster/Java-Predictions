package engine.factory;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.jaxb.schema.generated.PRDEntity;
import engine.world.utils.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityFactory {
    public static Entity createEntity(EntityDefinition entityDefinition) {
        Map<String, Property> entityProperties = PropertyFactory.createPropertyList(entityDefinition);
        return new Entity(entityDefinition.getName(), entityProperties);
    }

    public static EntityDefinition createEntityDefinition(PRDEntity prdEntity) {

        Map<String, Property> entityProperties = PropertyFactory.createPropertyList(prdEntity);
        return new EntityDefinition(prdEntity.getName(), entityProperties, prdEntity.getPRDPopulation());
    }

    public static List<EntityDefinition> createEntityDefinitionList(List<PRDEntity> prdEntity) {
        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        for (PRDEntity entity : prdEntity) {
            entityDefinitions.add(createEntityDefinition(entity));
        }
        return entityDefinitions;

    }

    public static List<Entity> createEntityList(EntityDefinition entityDefinition) {
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < entityDefinition.getPopulation(); i++) {
            entities.add(createEntity(entityDefinition));
        }
        return entities;
    }
}
