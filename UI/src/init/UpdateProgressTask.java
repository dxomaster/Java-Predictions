package init;

import engine.Engine;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;

public class UpdateProgressTask extends javafx.concurrent.Task<Void> {

    Engine engine;
    String UUID;

    IntegerProperty currentTicksProperty;
    IntegerProperty secondsProperty;
    Integer maxTicks;
    Integer maxSeconds;


    UpdateProgressTask(Engine engine, String UUID, IntegerProperty currentTicksProperty,
                       IntegerProperty secondsProperty, Integer maxTicks, Integer maxSeconds) {
        this.engine = engine;
        this.UUID = UUID;
        this.currentTicksProperty = currentTicksProperty;
        this.secondsProperty = secondsProperty;
        this.maxTicks = maxTicks;
        this.maxSeconds = maxSeconds;


    }

    @Override
    protected Void call() throws Exception {
        int currentTicks, currentSeconds;
        while (engine.isSimulationRunning(UUID)) {
            currentTicks = engine.getCurrentTickByUUID(UUID);
            currentSeconds = engine.getCurrentSecondByUUID(UUID);
            int finalTicks = currentTicks;
            int finalCurrentSeconds = currentSeconds;
            Platform.runLater(() ->
            {
                currentTicksProperty.setValue(finalTicks);
                secondsProperty.setValue(finalCurrentSeconds);
            });
            Thread.sleep(200);
        }
        return null;
    }
}
