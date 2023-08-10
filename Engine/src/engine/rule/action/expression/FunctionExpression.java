package engine.rule.action.expression;

import Exception.ERROR.ErrorException;
import engine.entity.Entity;
import engine.world.World;
import engine.world.utils.Property;
import engine.world.utils.PropertyType;

import java.util.Arrays;
import java.util.Random;

public class FunctionExpression implements Expression {
    functionEnum function;
    private final Object[] arguments;

    private final World world;

    public FunctionExpression(World world, String functionName, Object[] arguments) {
        this.world = world;
        this.arguments = new Object[1];
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
            default:
                throw new RuntimeException("Function " + function.functionInString + " not found");
        }
    }

    @Override
    public Object evaluate(World world, Entity entity) throws ErrorException {
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = world.getEnvironmentVariableByName((String) arguments[0]);
                assert envVariable != null;
                return envVariable.getValue();
            case RANDOM:
                Random random = new Random();
                return random.nextInt((int) arguments[0]);
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
