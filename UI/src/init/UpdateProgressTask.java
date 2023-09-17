package init;

import engine.Engine;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;

public class UpdateProgressTask extends javafx.concurrent.Task<Void> {

    Engine engine;
    String UUID;

    IntegerProperty currentTicksProperty;
    IntegerProperty secondsProperty;
    IntegerProperty maxTicks;
    IntegerProperty maxSeconds;


    UpdateProgressTask(Engine engine, String UUID, IntegerProperty currentTicksProperty,
                       IntegerProperty secondsProperty, IntegerProperty maxTicks, IntegerProperty maxSeconds) {
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
        currentTicks = engine.getCurrentTickByUUID(UUID);
        currentSeconds = engine.getCurrentSecondByUUID(UUID);
        int finalCurrentTicks = currentTicks;
        int finalCurrentSeconds1 = currentSeconds;
        Platform.runLater(() ->
        {
            currentTicksProperty.setValue(finalCurrentTicks);
            secondsProperty.setValue(finalCurrentSeconds1);
        });
        return null;
    }

    public IntegerProperty getCurrentTicksProperty() {
        return currentTicksProperty;
    }

    public IntegerProperty getMaxTicksProperty() {
        return maxTicks;
    }

    public IntegerProperty getCurrentSecondsProperty() {
        return secondsProperty;

    }

    public IntegerProperty getMaxSecondsProperty() {
        return maxSeconds;
    }
}
