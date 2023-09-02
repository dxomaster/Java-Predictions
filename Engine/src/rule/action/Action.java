package rule.action;

import DTO.ActionDTO;
import DTO.ActionableDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import entity.EntityDefinition;
import rule.action.expression.Expression;
import world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action implements Actionable, java.io.Serializable {
    //private final EntityDefinition secondaryEntityDefinition;
    private final SecondaryEntitySelection secondaryEntitySelection;
    private final EntityDefinition mainEntityDefinition;
    private final ActionNames action;
    private final Expression[] expressions;
    private final String propertyNameInString;
    private CalculationOperator operator;

    public Action(EntityDefinition mainEntityDefinition, String propertyNameInString, ActionNames action, SecondaryEntitySelection secondaryEntitySelection, Expression... expressions) {
        this.mainEntityDefinition = mainEntityDefinition;
        this.action = action;
        this.secondaryEntitySelection = secondaryEntitySelection;
        this.expressions = expressions;
        this.propertyNameInString = propertyNameInString;

    }

    public Action(EntityDefinition mainEntityDefinition, String propertyNameInString, CalculationOperator operator, ActionNames action, SecondaryEntitySelection secondaryEntitySelection, Expression... expressions) {
        this(mainEntityDefinition, propertyNameInString, action, secondaryEntitySelection, expressions);
        this.operator = operator;

    }

    public void performAction(World world, Entity entity, int ticks,Entity secondaryEntity) throws WarnException, ErrorException {
        switch (action) {
            case INCREASE:
                entity.increaseProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity),ticks);
                break;
            case DECREASE:
                entity.decreaseProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity),ticks);
                break;
            case SET:
                entity.setProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity),ticks);
                break;
            case CALCULATION:
                if (this.operator == CalculationOperator.MULTIPLY) {
                    entity.multiplyProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity), expressions[1].evaluate(world, entity,secondaryEntity),ticks);
                } else if (this.operator == CalculationOperator.DIVIDE) {
                    entity.divideProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity), expressions[1].evaluate(world, entity,secondaryEntity),ticks);
                }
                entity.setProperty(propertyNameInString, expressions[0].evaluate(world, entity,secondaryEntity),ticks);
                break;
            case KILL:
                entity.kill();
                break;
        }

    }

    @Override
    public SecondaryEntitySelection getSecondaryEntitySelection() {
        return secondaryEntitySelection;
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(mainEntityDefinition.getName());
        return entities;
    }

    @Override
    public String getName() {
        return action.actionInString;
    }

    @Override
    public ActionableDTO getActionableDTO() {
        String[] expressions = new String[this.expressions.length];
        for (int i = 0; i < this.expressions.length; i++) {
            expressions[i] = this.expressions[i].toString();
        }
        if(this.operator == null)
            return new ActionDTO(mainEntityDefinition.getName(),action.actionInString,expressions, propertyNameInString, "none");
        return new ActionDTO(mainEntityDefinition.getName(),action.actionInString,expressions, propertyNameInString, operator.name());
    }


    @Override
    public String toString() {
        return "Action{" +
                "operator=" + operator +
                ", entityName=" + mainEntityDefinition.getName() +
                ", action=" + action +
                ", expressions=" + Arrays.toString(expressions) +
                ", propertyNameInString='" + propertyNameInString + '\'' +
                '}';
    }
}


