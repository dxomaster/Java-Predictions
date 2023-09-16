package DTO;

public class ProximityDTO implements ActionableDTO {
    public String getSourceEntityName() {
        return sourceEntityName;
    }

    public String getTargetEntityName() {
        return targetEntityName;
    }

    public String getDepth() {
        return depth;
    }

    public String getActionName() {
        return actionName;
    }

    public String getNumberOfActions() {
        return numberOfActions;
    }

    private final String sourceEntityName;
    private final String targetEntityName;
    private final String depth;
    private final String actionName;
    private final String numberOfActions;

    public ProximityDTO(String sourceEntityName, String targetEntityName, String depth, String actionName, String numberOfActions) {
        this.sourceEntityName = sourceEntityName;
        this.targetEntityName = targetEntityName;
        this.depth = depth;
        this.actionName = actionName;
        this.numberOfActions = numberOfActions;
    }

    @Override
    public String getName() {
        return actionName;
    }

    @Override
    public String getSecondaryEntityName() {
        return "none";
    }
}
