package engine.rule.action;

import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.expression.Expression;
import engine.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action implements Actionable {
    private final EntityDefinition entityDefinition;
    private final ActionNames action;
    private final Expression[] expressions;
    private final String propertyNameInString;
    private CalculationOperator operator;

    public Action(EntityDefinition entityDefinition, String propertyNameInString, ActionNames action, Expression... expressions) {
        this.entityDefinition = entityDefinition;
        this.action = action;
        this.expressions = expressions;
        this.propertyNameInString = propertyNameInString;

    }

    public Action(EntityDefinition entityDefinition, String propertyNameInString, CalculationOperator operator, ActionNames action, Expression... expressions) {
        this(entityDefinition, propertyNameInString, action, expressions);
        this.operator = operator;

    }

    public void performAction(World world, Entity entity) throws WarnException, ErrorException {
        switch (action) {
            case INCREASE:
                entity.increaseProperty(propertyNameInString, expressions[0].evaluate(world, entity));
                break;
            case DECREASE:
                entity.decreaseProperty(propertyNameInString, expressions[0].evaluate(world, entity));
                break;
            case SET:
                entity.setProperty(propertyNameInString, expressions[0].evaluate(world, entity));
                break;
            case CALCULATION:
                if (this.operator == CalculationOperator.MULTIPLY) {
                    entity.multiplyProperty(propertyNameInString, expressions[0].evaluate(world, entity), expressions[1].evaluate(world, entity));
                } else if (this.operator == CalculationOperator.DIVIDE) {
                    entity.divideProperty(propertyNameInString, expressions[0].evaluate(world, entity), expressions[1].evaluate(world, entity));
                }
                entity.setProperty(propertyNameInString, expressions[0].evaluate(world, entity));
                break;
            case KILL:
                entity.kill();
                break;
        }

    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(entityDefinition.getName());
        return entities;
    }

    @Override
    public String getName() {
        return action.actionInString;
    }


    @Override
    public String toString() {
        return "Action{" +
                "operator=" + operator +
                ", entityName=" + entityDefinition.getName() +
                ", action=" + action +
                ", expressions=" + Arrays.toString(expressions) +
                ", propertyNameInString='" + propertyNameInString + '\'' +
                '}';
    }
}


