package init;

import DTO.RunEndDTO;
import DTO.RunStatisticsDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class ResultsController implements Initializable {
    Engine engine;
    @FXML
    GridPane gridPane;
    @FXML
    ComboBox<String> viewSelection;
    ListView<String> listView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
        viewSelection.getItems().addAll("Run Progress","Entity Information", "Statistics");

        try {
            List<RunEndDTO> runEndDTOS = engine.getPastArtifacts();
            listView = new ListView<>();
            ObservableList<String> items = FXCollections.observableArrayList();
            if (runEndDTOS.isEmpty()) {
                items.add("No runs have been completed yet.");
            }
            for (RunEndDTO runEndDTO : runEndDTOS) {
                items.add(runEndDTO.toString());
                if(runEndDTO.getStatus().equals("Completed")) {
                    //only calculate statistics for completed runs
                    RunStatisticsDTO stat = engine.getPastSimulationStatisticsDTO(runEndDTO.getUUID());
                }

            }
            listView.setItems(items);
            GridPane.setConstraints(listView, 0, 0,1,3);
            gridPane.add(listView, 0, 0);
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String runUUID = listView.getSelectionModel().getSelectedItem().split("\n")[0].split(":")[1].trim();
                    viewRunProgress(runUUID);
                    System.out.println("Selected item: " + newValue);
                }
            });


        } catch (Exception e) {
            showErrorAlert(e);
        }
    }
    private void viewRunProgress(String runUUID){//todo here
        gridPane.getChildren().clear();
        gridPane.add(listView, 0, 0);
        gridPane.add(viewSelection, 1, 0);
        WorldDTO dto = engine.getWorldDTOByUUID(runUUID);
        IntegerProperty currentTick = new SimpleIntegerProperty();
        IntegerProperty secondsProperty = new SimpleIntegerProperty();
        Label currentTickLabel = new Label("Current tick: ");
        Label secondsLabel = new Label("Seconds elapsed: ");
        currentTickLabel.textProperty().bind(Bindings.format("Current tick: %d", currentTick));
        secondsLabel.textProperty().bind(Bindings.format("Seconds elapsed: %d", secondsProperty));
        gridPane.add( currentTickLabel, 1, 0);
        gridPane.add( secondsLabel, 1, 1);
        UpdateProgressTask task = new UpdateProgressTask(engine, runUUID, currentTick, secondsProperty,dto.getTermination().getTicks(), dto.getTermination().getSeconds() );
        Thread taskThread = new Thread(task);
        taskThread.setDaemon(true);
        taskThread.start();



    }
}
