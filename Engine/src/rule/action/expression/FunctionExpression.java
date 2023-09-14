package rule.action.expression;

import entity.Entity;
import entity.EntityDefinition;
import factory.ExpressionFactory;
import world.World;
import world.utils.Property;
import world.utils.PropertyType;

import java.util.Arrays;
import java.util.Random;

public class FunctionExpression implements Expression, java.io.Serializable {
    private final functionEnum function;
    private final Object[] arguments;
    private final String entityName;

    private final World world;

    public FunctionExpression(World world, String functionName, Object[] arguments,String entityName){
        this.world = world;
        String entity, property;
        this.entityName = entityName;
        Property p;
        this.arguments = new Object[2];
        if (arguments == null) throw new IllegalArgumentException("Arguments cannot be null");
        try {
            this.function = functionEnum.valueOf(functionName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Function " + functionName + " not found");
        }


        switch (function) {
            case ENVIRONMENT:
                this.arguments[0] = arguments[0];
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                if (envVariable == null) {
                    throw new RuntimeException("Environment variable " + arguments[0] + " not found");
                }
                break;
            case RANDOM:
                this.arguments[0] = Integer.parseInt((String) arguments[0]);
                break;
            case EVALUATE:
                entity = getEntityFromArgument((String) arguments[0]);
                property = getPropertyFromArgument((String) arguments[0]);
                p = world.getEntityDefinitionByName(entity).getPropertyByName(property);
                this.arguments[0] = p;
                break;
            case PERCENT:
                Expression e1 = ExpressionFactory.createExpression(world,entityName,(String) arguments[0]);
                Expression e2 = ExpressionFactory.createExpression(world,entityName,(String) arguments[1]);
                if (e1.isNotNumber() || e2.isNotNumber())
                    throw new RuntimeException("Expression " + arguments[0] + " or " + arguments[1] + " is not a number," +
                            " and is defined inside percent function.");
                this.arguments[0] = e1;
                this.arguments[1] = e2;
                break;
            case TICKS:
                entity = getEntityFromArgument((String) arguments[0]);
                property = getPropertyFromArgument((String) arguments[0]);
                p = world.getEntityDefinitionByName(entity).getPropertyByName(property);
                if (p == null) throw new RuntimeException("Property " + arguments[0] + " not found");
                this.arguments[0] = p;
                break;

            default:
                throw new RuntimeException("Function " + function.functionInString + " not found");
        }
    }
    private String getEntityFromArgument(String argument)
    {
        return argument.substring(0,argument.indexOf("."));
    }
    private String getPropertyFromArgument(String argument)
    {
        return argument.substring(argument.indexOf(".")+1);
    }

    @Override
    public boolean isNotNumber() {
        EntityDefinition entityDefinition = world.getEntityDefinitionByName(entityName);
        Entity mockEntity = new Entity(entityDefinition.getName(), entityDefinition.getProperties());
        Object value = evaluate(world, mockEntity,null);
        return !(value instanceof Integer) && !(value instanceof Float);

    }

    @Override
    public Object evaluate(World world, Entity entity,Entity secondaryEntity) {
        Property p;
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                assert envVariable != null;
                return envVariable.getValue();
            case RANDOM:
                Random random = new Random();
                return (float)random.nextInt((int) arguments[0]);
            case EVALUATE:
                p = (Property) arguments[0];
                p = entity.getPropertyByName(p.getName());
                return p.getValue();
            case PERCENT:
               float whole =  (float)((Expression) arguments[0]).evaluate(world,entity,secondaryEntity);
                float part = (float)((Expression) arguments[1]).evaluate(world,entity,secondaryEntity);
                return part/whole;
            case TICKS:
                p = (Property) arguments[0];
                p = entity.getPropertyByName(p.getName());
                return (float)world.getTicks() - p.getLastUpdatedTick();

            default:
                return null;
        }
    }
    @Override
    public PropertyType getType() {
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                assert envVariable != null;
                return envVariable.getType();
            case RANDOM:
            case TICKS:
                return PropertyType.FLOAT;
            case EVALUATE:
                return ((Property) arguments[0]).getType();
            case PERCENT:
                return PropertyType.FLOAT;

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        String arguments[];
        int length = 0;
        for (Object argument : this.arguments) {
            if (argument != null)
                length++;
        }
        arguments = new String[length];
        for (int i = 0; i < length; i++) {
            arguments[i] = this.arguments[i].toString();
        }

        return "Function Expression: " + function +
                ", Arguments: " + Arrays.toString(arguments);
    }
}
