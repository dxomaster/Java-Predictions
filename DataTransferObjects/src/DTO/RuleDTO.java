package DTO;

import java.util.List;

public class RuleDTO {
    private String name;
    private float probability;
    private float ticks;
    private int numberOfActions;
    private List<ActionableDTO> actions;

    public String getName() {
        return name;
    }

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public List<ActionableDTO> getActions() {
        return actions;
    }

    public float getProbability() {
        return probability;
    }

    public float getTicks() {
        return ticks;
    }

    public RuleDTO(String name, float probability, float ticks, int numberOfActions, List<ActionableDTO> actions) {
        this.name = name;
        this.probability = probability;
        this.ticks = ticks;
        this.numberOfActions = numberOfActions;
        this.actions = actions;
    }
}
