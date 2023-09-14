package DTO;

public class ReplaceDTO implements ActionableDTO {
    private final String entityToKill;

    public String getEntityToKill() {
        return entityToKill;
    }

    public String getEntityToCreate() {
        return entityToCreate;
    }

    public String getMode() {
        return mode;
    }

    public String getActionName() {
        return actionName;
    }

    private final String entityToCreate;
    private final String mode;
    private final String actionName;

    public ReplaceDTO(String entityToKill, String entityToCreate, String mode, String actionName) {
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.mode = mode;
        this.actionName = actionName;
    }

    @Override
    public String getName() {
        return actionName;
    }
}
