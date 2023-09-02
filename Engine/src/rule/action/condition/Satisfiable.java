package rule.action.condition;

import DTO.SatisfiableDTO;
import Exception.ERROR.ErrorException;
import entity.Entity;
import world.World;

import java.util.List;

public interface Satisfiable {
    boolean isSatisfied(World world, Entity entity,Entity secondaryEntity) throws ErrorException;

    List<String> getEntities();

    SatisfiableDTO getSatisfiableDTO();
}
