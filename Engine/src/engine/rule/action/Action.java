package engine.rule.action;

import engine.entity.Entity;
import engine.entity.EntityDefinition;
import engine.rule.action.expression.Expression;

public class Action implements Actionable {
    protected CalculationOperator operator;
    protected EntityDefinition entityDefinition;
    protected Expression secondExpressionOptional;
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

    public void performAction(Entity entity) {
                switch (action) {
                    case INCREASE:
                        entity.increaseProperty(propertyNameInString, expressions[0].evaluate());
                        break;
                    case DECREASE:
                        entity.decreaseProperty(propertyNameInString, expressions[0].evaluate());
                        break;
                    case SET:
                        entity.setProperty(propertyNameInString, expressions[0].evaluate());
                        break;
                    case CALCULATION:
                        if(this.operator == CalculationOperator.MULTIPLY) {
                            entity.multiplyProperty(propertyNameInString, expressions[0].evaluate(),expressions[1].evaluate());
                        } else if(this.operator == CalculationOperator.DIVIDE) {
                            entity.divideProperty(propertyNameInString, expressions[0].evaluate(),expressions[1].evaluate());
                        }
                        entity.setProperty(propertyNameInString, expressions[0].evaluate());
                        break;
                    case KILL:
                        entity.kill();
                        break;
                }

        }
    }


