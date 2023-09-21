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
import javafx.scene.Node;
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
    @FXML
    Label currentTicks;
    @FXML
    Label maxTicks;
    @FXML
    ProgressBar ticksProgressBar;
    @FXML
    private ListView simulations;
    ResourceBundle resources;
    private String currentUUID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.engine = (Engine) resources.getObject("Engine");
        viewSelection.getItems().addAll("Run Progress","Entity Information", "Statistics");

        try {
            simulations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String runUUID = simulations.getSelectionModel().getSelectedItem().toString().split("\n")[0].split(":")[1].trim();
                    if(!runUUID.equals(currentUUID)) {
                        selectRun(runUUID);
                        changeView();
                    }
                }
            });

            UpdateRunListTask updateRunListTask = new UpdateRunListTask(engine, simulations);
            Thread thread = new Thread(updateRunListTask);
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }
    private void selectRun(String runUUID){
        this.currentUUID = runUUID;
    }
    @FXML
    private void changeView()
    {
        String selected = viewSelection.getSelectionModel().getSelectedItem();
        if(selected != null) {
            if(currentUUID == null){
                showErrorAlert(new Exception("No run selected"));
                return;
            }
            try {
                gridPane.getChildren().remove(2);
            }
            catch (Exception ignored){
            }
            switch (selected) {
                case "Run Progress":
                    viewRunProgress();
                    break;
                case "Entity Information":
                    viewEntityInformation();
                    break;
                case "Statistics":
                    viewStatistics(this.currentUUID);
                    break;
            }
        }
    }

    private void viewStatistics(String currentUUID) {
        if (engine.isSimulationRunning(currentUUID)) {
            showErrorAlert(new Exception("Simulation is still running"));
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("Statistics.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(this);
            Parent root = loader.load();
            StatisticsController statisticsController = loader.getController();
            gridPane.add(root, 1, 0);
        }
        catch (Exception e)
        {
            showErrorAlert(e);
        }
    }

    private void viewEntityInformation() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("EntityInformation.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(this);
            Parent root = loader.load();
            EntityInformationController entityInformationController = loader.getController();

            gridPane.add(root, 1, 0);
        }
        catch (Exception e)
        {
            showErrorAlert(e);
        }

    }

    private void viewRunProgress(){//todo here
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("ViewProgress.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(this);
            Parent root = loader.load();
            ProgressViewController progressViewController = loader.getController();
            gridPane.add(root, 1, 0);
        }
        catch (Exception e)
        {
            showErrorAlert(e);
        }
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
