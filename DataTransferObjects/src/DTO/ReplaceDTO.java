package DTO;

public class ReplaceDTO implements ActionableDTO {
    private final String entityToKill;
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
