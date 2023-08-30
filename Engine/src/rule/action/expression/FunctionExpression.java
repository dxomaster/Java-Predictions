package rule.action.expression;

import entity.Entity;
import factory.ExpressionFactory;
import world.World;
import world.utils.Property;
import world.utils.PropertyType;

import java.util.Arrays;
import java.util.Random;

public class FunctionExpression implements Expression, java.io.Serializable {
    private final functionEnum function;
    private final Object[] arguments;

    private final World world;

    public FunctionExpression(World world, String functionName, Object[] arguments,String entityName,PropertyType type){
        this.world = world;
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

                break;
            case PERCENT:
                if(type != PropertyType.FLOAT) throw new RuntimeException("Percent function can only be used with float type");
                Expression e1 = ExpressionFactory.createExpression(world,entityName,type,(String) arguments[0]);
                Expression e2 = ExpressionFactory.createExpression(world,entityName,type,(String) arguments[1]);
                this.arguments[0] = e1;
                this.arguments[1] = e2;
                break;
            case TICKS:
                String entity = getTicksEntity((String) arguments[0]);
                String property = getTicksProperty((String) arguments[0]);
                Property p = world.getEntityDefinitionByName(entity).getPropertyByName(property);
                this.arguments[0] = p;
                break;

            default:
                throw new RuntimeException("Function " + function.functionInString + " not found");
        }
    }
    private String getTicksEntity(String argument)
    {
        return argument.substring(0,argument.indexOf("."));
    }
    private String getTicksProperty(String argument)
    {
        return argument.substring(argument.indexOf(".")+1);
    }
    @Override
    public Object evaluate(World world, Entity entity) {
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                assert envVariable != null;
                return envVariable.getValue();
            case RANDOM:
                Random random = new Random();
                return random.nextInt((int) arguments[0]);
            case EVALUATE:
                return null;
            case PERCENT:
               float whole =  (float)((Expression) arguments[0]).evaluate(world,entity);
                float part = (float)((Expression) arguments[1]).evaluate(world,entity);
                return part/whole;
            case TICKS:
                Property p = (Property) arguments[0];
                return world.getTicks() - p.getLastUpdatedTick();

            default:
                return null;
        }
    }

    public PropertyType getType() {
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                assert envVariable != null;
                return envVariable.getType();
            case RANDOM:
                return PropertyType.DECIMAL;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "FunctionExpression{" +
                "function=" + function +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
