package engine.rule.action;

import engine.entity.Entity;
import engine.rule.action.condition.Condition;
import engine.rule.action.expression.Expression;

public class Action {
    CalculationOperator operator;
    Condition condition;
    Entity entity;
    Actions actionToPreform;
    Expression[] expression;
    String propertyNameInString;
public Action(Entity entity, Actions actionToPreform, String propertyNameInString, Condition condition, Expression ... expression) {
        this.entity = entity;
        this.actionToPreform = actionToPreform;
        this.expression = expression;
        this.propertyNameInString = propertyNameInString;
        this.condition = condition;
    }public Action(Entity entity, Actions actionToPreform, String propertyNameInString,CalculationOperator operator, Condition condition, Expression ... expression) {
        this.entity = entity;
        this.actionToPreform = actionToPreform;
        this.expression = expression;
        this.propertyNameInString = propertyNameInString;
        this.condition = condition;
        this.operator = operator;
    }
    public void preformAction() {
        if(condition == null || condition.isSatisfied(entity)) {
            switch (actionToPreform) {
                case INCREASE:
                    entity.increaseProperty(propertyNameInString, expression[0].evaluate());
                    break;
                case DECREASE:
                    entity.decreaseProperty(propertyNameInString, expression[0].evaluate());
                    break;
                case SET:
                    entity.setProperty(propertyNameInString, expression[0].evaluate());
                    break;
                case CALCULATION:
                    if(this.operator == CalculationOperator.MULTIPLY) {
                        entity.multiplyProperty(propertyNameInString, expression[0].evaluate(),expression[1].evaluate());
                    } else if(this.operator == CalculationOperator.DIVIDE) {
                        entity.divideProperty(propertyNameInString, expression[0].evaluate(),expression[1].evaluate());
                    }
                    entity.setProperty(propertyNameInString, expression[0].evaluate());
                    break;
                case KILL:
                    entity.kill();
                    break;
            }
        }
    }

}
