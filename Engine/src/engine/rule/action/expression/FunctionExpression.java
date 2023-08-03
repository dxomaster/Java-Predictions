package engine.rule.action.expression;

import engine.world.World;
import engine.world.utils.Property;

import java.util.Random;

public class FunctionExpression implements Expression {
    functionEnum function;
    private Object[] arguments;

    public FunctionExpression(String functionName, Object[] arguments) {
        if (arguments == null) throw new IllegalArgumentException("Arguments cannot be null");
        try {
            this.function = functionEnum.valueOf(functionName.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Function " + functionName + " not found");
        }
        this.arguments = arguments;
    }

    @Override
    public Object evaluate() {
        switch (function) {
            case ENVIRONMENT:
                Property envVariable = World.getEnvironmentVariableByName((String) arguments[0]);
                if (envVariable == null) {
                    throw new RuntimeException("Environment variable " +  arguments[0] + " not found");
                }
                return envVariable.getValue();
            case RANDOM:
                Random random = new Random();
                if (!(arguments[0] instanceof Integer))
                    throw new RuntimeException("Argument of random function must be an integer");
                return random.nextInt((int) arguments[0]);
            default:
                throw new RuntimeException("Function " + function.functionInString + " not found");
        }
    }


}
