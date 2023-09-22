package init;

import engine.Engine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Enumeration;
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
    private HBox dynamicDisplay;
    private CheckBox showAnimations;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.engine = (Engine) resources.getObject("Engine");
        this.dynamicDisplay = (HBox) resources.getObject("dynamicDisplay");
        this.showAnimations = (CheckBox) resources.getObject("showAnimations");
        viewSelection.getItems().addAll("Run Progress","Entity Information", "Statistics");
        viewSelection.getSelectionModel().selectFirst();
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
    @FXML
    private void runSimulationAgain() throws InterruptedException {
        this.engine.runSimulationAgain(currentUUID);
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("Execution.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(this);
            Parent root = loader.load();
            ExecutionController executionController = loader.getController();
            dynamicDisplay.getChildren().clear();
            dynamicDisplay.getChildren().add(root);

        }
        catch (Exception e)
        {
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
                gridPane.getChildren().remove(3);
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
            case "dynamicDisplay":
                return this.dynamicDisplay;
            case "showAnimations":
                return this.showAnimations;
            default:
                return null;
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }
}
