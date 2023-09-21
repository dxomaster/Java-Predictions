package rule.action;

import DTO.ActionableDTO;
import DTO.ReplaceDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class Replace implements Actionable{
    private final String entityToKill;
    private final String entityToCreate;
    private final String mode;
    private final ActionNames action;

    public Replace(String entityToKill, String entityToCreate, String mode, ActionNames action) {
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.mode = mode;
        this.action = action;
    }
    @Override
    public void performAction(World world, Entity entity, int ticks,Entity secondaryEntity) throws WarnException, ErrorException {
        entity.kill();
        if (mode.equals("scratch")) {
            world.createEntityFromScratch(entityToCreate);
        } else if (mode.equals("derived")) {
            world.createEntityDerived(entityToCreate, entity);
        } else {
            throw new ErrorException(mode + " is not a valid mode. Please use either scratch or derived");
        }
    }

    @Override
    public SecondaryEntitySelection getSecondaryEntitySelection() {
        return null;
    }

    @Override
    public List<String> getEntities() {
        List<String> entities = new ArrayList<>();
        entities.add(entityToKill);
        return entities;
    }

    @Override
    public String getName() {
        return "Replace";
    }

    @Override
    public ActionableDTO getActionableDTO() {
        return new ReplaceDTO(entityToKill, entityToCreate, mode, action.actionInString);
    }
}
