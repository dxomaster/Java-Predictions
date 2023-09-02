package rule.action;

import DTO.ActionableDTO;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import entity.Entity;
import world.World;

import java.util.List;

public interface Actionable {
    void performAction(World world, Entity entity, int tick, Entity secondaryEntity) throws WarnException, ErrorException;
    SecondaryEntitySelection getSecondaryEntitySelection();
    List<String> getEntities();

    String getName();

    ActionableDTO getActionableDTO();
}
