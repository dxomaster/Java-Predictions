package init;

import engine.Engine;
import javafx.application.Platform;

import static init.PredictionsController.showInfoMessage;

public class NotifyWhenSimulationIsFinishedTask extends javafx.concurrent.Task<Void> {

    private final Engine engine;
    private final String UUID;

    public NotifyWhenSimulationIsFinishedTask(Engine engine, String UUID){
        this.engine = engine;
        this.UUID = UUID;
    }
    @Override
    protected Void call() throws Exception {
        while(engine.isSimulationRunning(UUID))
        {
            Thread.sleep(200);
        }
        Platform.runLater( () -> showInfoMessage("Simulation " + UUID + " Finished!"));
        return null;
    }
}
