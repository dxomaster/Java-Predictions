package init;

import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ProgressViewController implements Initializable {
    private String UUID;
    @FXML
    private Label currentTicksLabel;
    @FXML
    private Label maxTicksLabel;
    @FXML
    private Label currentSecondsLabel;
    @FXML
    private Label maxSecondsLabel;
    @FXML
    private ProgressBar progressBar;
    private Engine engine;
    private static final Map<String,UpdateProgressTask> tasks = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.engine = (Engine) resources.getObject("Engine");
        this.UUID = (String) resources.getObject("UUID");
        IntegerProperty currentTicks = new SimpleIntegerProperty();
        IntegerProperty maxTicks = new SimpleIntegerProperty();
        IntegerProperty currentSeconds = new SimpleIntegerProperty();
        IntegerProperty maxSeconds = new SimpleIntegerProperty(1);
        if(!tasks.containsKey(UUID)){
            if(engine.getMaxTickByUUID(UUID) == null){
                maxTicks.set(-1);
            }
            else
            {
                maxTicks.set(engine.getMaxTickByUUID(UUID));
            }
            if(engine.getMaxSecondsByUUID(UUID) == null){
                maxSeconds.set(-1);
            }
           else
                maxSeconds.set(engine.getMaxSecondsByUUID(UUID));
            UpdateProgressTask updateProgressTask = new UpdateProgressTask(engine, UUID, currentTicks, currentSeconds, maxTicks, maxSeconds);
            tasks.put(UUID, updateProgressTask);
            Thread thread = new Thread(updateProgressTask);
            thread.setDaemon(true);
            thread.start();
        }
        else {
            //task already exists
            UpdateProgressTask updateProgressTask = tasks.get(UUID);
             currentTicks = updateProgressTask.getCurrentTicksProperty();
             maxTicks = updateProgressTask.getMaxTicksProperty();
             currentSeconds = updateProgressTask.getCurrentSecondsProperty();
             maxSeconds = updateProgressTask.getMaxSecondsProperty();
            if(engine.getMaxSecondsByUUID(UUID) == null){
                maxSeconds.set(-1);
            }

        }
        currentTicksLabel.textProperty().bind(Bindings.format("Current ticks: %d" , currentTicks));
        if(maxTicks.get() == -1)
            maxTicksLabel.textProperty().bind(Bindings.format("Max ticks: N/A" ));
        else
            maxTicksLabel.textProperty().bind(Bindings.format("Max ticks: %d" , maxTicks));
        maxTicksLabel.textProperty().bind(Bindings.format("Max ticks: %d" , maxTicks));
        currentSecondsLabel.textProperty().bind(Bindings.format("Current Seconds: %d" , currentSeconds));
        if(maxSeconds.get() == -1)
            maxSecondsLabel.textProperty().bind(Bindings.format("Max seconds: N/A" ));
        else
            maxSecondsLabel.textProperty().bind(Bindings.format("Max seconds %d" , maxSeconds));
        DoubleProperty divisionResultProperty = new SimpleDoubleProperty();
        IntegerProperty finalCurrentTicks = currentTicks;
        IntegerProperty finalMaxTicks = maxTicks;
        divisionResultProperty.bind(
                Bindings.createDoubleBinding(
                        () -> (double) finalCurrentTicks.get() / finalMaxTicks.get(),
                        currentTicks,
                        maxTicks
                )
        );
        progressBar.progressProperty().bind(divisionResultProperty);
    }

    public void setUUID(String runUUID) {
        this.UUID = runUUID;
    }
}
