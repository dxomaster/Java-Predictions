package init;

import DTO.RunEndDTO;
import DTO.RunStatisticsDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class ResultsController extends ResourceBundle implements Initializable {
    Engine engine;
    @FXML
    GridPane gridPane;
    @FXML
    ComboBox<String> viewSelection;
    ListView<String> listView;
    @FXML
    Label currentTicks;
    @FXML
    Label maxTicks;
    @FXML
    ProgressBar ticksProgressBar;
    ResourceBundle resources;
    private String currentUUID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
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
        try {
            this.currentUUID = runUUID;
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("ViewProgress.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(this);
            Parent root = loader.load();
            ProgressViewController progressViewController = loader.getController();
            //progressViewController.setUUID(runUUID);
            //gridPane.getChildren().clear();
            gridPane.add(root, 1, 1);
        }
        catch (Exception e)
        {
            showErrorAlert(e);
        }
//        //gridPane.getChildren().clear();
//        //gridPane.add(listView, 0, 0);
//        //gridPane.add(viewSelection, 1, 0);
//        WorldDTO dto = engine.getWorldDTOByUUID(runUUID);
//        IntegerProperty currentTick = new SimpleIntegerProperty();
//        IntegerProperty secondsProperty = new SimpleIntegerProperty();
//        currentTicks.textProperty().bind(Bindings.format("Current tick: %d", currentTick));
//        ticksProgressBar.progressProperty().bind(currentTick.divide(dto.getTermination().getTicks()));
//        maxTicks.textProperty().bind(Bindings.format("Max ticks: %d", dto.getTermination().getTicks()));
//        //secondsLabel.textProperty().bind(Bindings.format("Seconds elapsed: %d", secondsProperty));
//        //gridPane.add( currentTicks, 1, 0);
//        //gridPane.add( secondsLabel, 1, 1);
//        //UpdateProgressTask task = new UpdateProgressTask(engine, runUUID, currentTick, secondsProperty,dto.getTermination().getTicks(), dto.getTermination().getSeconds() );
//        //Thread taskThread = new Thread(task);
//        //taskThread.setDaemon(true);
//        //taskThread.start();



    }

    @Override
    protected Object handleGetObject(String key) {
        switch (key)
        {
            case "Engine":
                return this.engine;
            case "UUID":
                return this.currentUUID;
            default:
                return null;
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }
}
