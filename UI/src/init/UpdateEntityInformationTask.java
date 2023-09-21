package init;

import DTO.EntityDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.collections.ObservableList;

public class UpdateEntityInformationTask extends javafx.concurrent.Task<Void> {
    private final Engine engine;
    private final String uuid;
    private final ObservableList<EntityDTO> entityDTOObservableList;

    public UpdateEntityInformationTask(Engine engine, String uuid, ObservableList<EntityDTO> entityDTOObservableList) {

        this.engine = engine;
        this.uuid = uuid;
        this.entityDTOObservableList = entityDTOObservableList;
    }

    @Override
    protected Void call() throws Exception {
        while (engine.isSimulationRunning(uuid)) {
            WorldDTO worldDTO = engine.getWorldDTOByUUID(uuid);
            entityDTOObservableList.clear();
            entityDTOObservableList.addAll(worldDTO.getEntities());
            Thread.sleep(200);
        }
        return null;
    }
}
