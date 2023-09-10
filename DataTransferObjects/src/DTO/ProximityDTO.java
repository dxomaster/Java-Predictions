package DTO;

public class ProximityDTO implements ActionableDTO {
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
}
