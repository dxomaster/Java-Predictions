package engine.rule.action;

import Exception.WARN.WarnException;
import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action implements Actionable {
    protected CalculationOperator operator;
    protected EntityDefinition entityDefinition;
    protected ActionNames action;
    protected Expression[] expressions;
    protected String propertyNameInString;
    public Action(EntityDefinition entityDefinition, String propertyNameInString, ActionNames action, Expression ... expressions) {
        this.entityDefinition = entityDefinition;
        this.action = action;
        this.expressions = expressions;
        this.propertyNameInString = propertyNameInString;

    }

    public Action(EntityDefinition entityDefinition, String propertyNameInString, CalculationOperator operator, ActionNames action , Expression ... expressions) {
        this(entityDefinition, propertyNameInString,action,expressions);
        this.operator = operator;

    }

    public void performAction(Entity entity) throws WarnException {
                switch (action) {
                    case INCREASE:
                        entity.increaseProperty(propertyNameInString, expressions[0].evaluate(entity));
                        break;
                    case DECREASE:
                        entity.decreaseProperty(propertyNameInString, expressions[0].evaluate(entity));
                        break;
                    case SET:
                        entity.setProperty(propertyNameInString, expressions[0].evaluate(entity));
                        break;
                    case CALCULATION:
                        if(this.operator == CalculationOperator.MULTIPLY) {
                            entity.multiplyProperty(propertyNameInString, expressions[0].evaluate(entity),expressions[1].evaluate(entity));
                        } else if(this.operator == CalculationOperator.DIVIDE) {
                            entity.divideProperty(propertyNameInString, expressions[0].evaluate(entity),expressions[1].evaluate(entity));
                        }
                        entity.setProperty(propertyNameInString, expressions[0].evaluate(entity));
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


