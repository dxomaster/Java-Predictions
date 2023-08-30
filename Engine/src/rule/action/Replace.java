package rule.action;

import DTO.ActionableDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import world.World;

import java.util.ArrayList;
import java.util.List;

public class Replace implements Actionable{
    private String entityToKill;
    private String entityToCreate;
    private String mode;
    public Replace(String entityToKill, String entityToCreate, String mode) {
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.mode = mode;
    }
    @Override
    public void performAction(World world, Entity entity, int ticks) throws WarnException, ErrorException {
    entity.kill();
    if (mode.equals("scratch"))
        {
            world.createEntityFromScratch(entityToCreate);
        }
    else
        {
            world.createEntityDerived(entityToCreate, entity);
        }

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
        return null;
    }
}
