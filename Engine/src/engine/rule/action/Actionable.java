package engine.rule.action;

import Exception.WARN.WarnException;
import engine.entity.Entity;

import java.util.List;

public interface Actionable {
    public void performAction(Entity entity) throws WarnException;
    public List<String> getEntities();
    public String getName();
}
