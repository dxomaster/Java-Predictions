package init;

import engine.Engine;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class PredictionsController extends ResourceBundle implements Initializable  {
        private Stage primaryStage;

        private Engine engine;
        private ScaleTransition hoverAnimation;
        @FXML
        private Label fileLoadedLabel;
        @FXML
        private Button loadFileButton;
        private String simulationName;

        @FXML
        private HBox dynamicDisplay;
        @FXML
        private Label queueInfoLabel;
        private UpdateQueuePoolTask updateQueuePoolTask;
        @FXML
        CheckBox showAnimations;

        public void setEngine(Engine engine) {
                this.engine = engine;
        }
        @Override
        public void initialize(URL url, ResourceBundle rb){

        }
        public static void showInfoMessage(String message)
        {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
        public void startUpdateQueueInfoLabelTask()
        {
                if(this.updateQueuePoolTask != null)
                {
                        this.updateQueuePoolTask.cancel();
                }
                UpdateQueuePoolTask updateQueuePoolTask = new UpdateQueuePoolTask(queueInfoLabel,engine);
                Thread thread = new Thread(updateQueuePoolTask);
                thread.setDaemon(true);
                thread.start();
        }
        @FXML
        protected void viewResults(ActionEvent event)
        {
                try {
                        FXMLLoader loader = new FXMLLoader();
                        URL mainFXML = getClass().getResource("Results.fxml");
                        loader.setLocation(mainFXML);
                        loader.setResources(this);
                        Parent root = loader.load();
                        if (this.showAnimations.isSelected()) {
                                // Apply the fade-in transition to the results screen
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), root);
                                fadeTransition.setFromValue(0.0);
                                fadeTransition.setToValue(1.0);
                                fadeTransition.play();
                        }
                        ResultsController resultsController = loader.getController();
                        dynamicDisplay.getChildren().clear();
                        dynamicDisplay.getChildren().add(root);
                }
                catch (Exception e)
                {
                        showErrorAlert(e);
                }
        }
        @FXML
        protected void loadFile(ActionEvent event){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Simulation File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile == null) {
                        return;
                }
                this.simulationName = selectedFile.getName();
                try {

                        this.engine.loadSimulationParametersFromFile(selectedFile.getAbsolutePath());
                        fileLoadedLabel.setText("Current File Loaded: " + selectedFile.getAbsolutePath());
                        this.engine.clearPastSimulations();
                } catch (Exception e) {
                        showErrorAlert(e);
                }

                startUpdateQueueInfoLabelTask();
                this.dynamicDisplay.getChildren().clear();
        }



        public static void showErrorAlert(Exception e)
        {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
        }


        public void setPrimaryStage(Stage primaryStage) {
                this.primaryStage = primaryStage;
        }


        private void checkIfFileLoaded() throws Exception {
                if(this.simulationName == null)
                {
                        throw new Exception("Please load a file first");
                }
        }
        @FXML
        protected void showSimulationDetails(ActionEvent event) {
                try {
                        checkIfFileLoaded();
                        FXMLLoader loader = new FXMLLoader();
                        URL mainFXML = getClass().getResource("SimulationDetails.fxml");
                        loader.setLocation(mainFXML);
                        loader.setResources(this);
                        Parent root = loader.load();
                        if (this.showAnimations.isSelected()) {
                                // Apply the fade-in transition to the results screen
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), root);
                                fadeTransition.setFromValue(0.0);
                                fadeTransition.setToValue(1.0);
                                fadeTransition.play();
                        }
                        DetailsController detailsController = loader.getController();
                        dynamicDisplay.getChildren().clear();
                        dynamicDisplay.getChildren().add(root);

                }
                catch (Exception e) {
                        showErrorAlert(e);
                        dynamicDisplay.getChildren().clear();
                }
        }
        @FXML
        protected void newExecution(ActionEvent event) {
                try {
                        checkIfFileLoaded();
                        FXMLLoader loader = new FXMLLoader();
                        URL mainFXML = getClass().getResource("Execution.fxml");
                        loader.setLocation(mainFXML);
                        loader.setResources(this);
                        Parent root = loader.load();
                        if (this.showAnimations.isSelected()) {
                                // Apply the fade-in transition to the results screen
                                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), root);
                                fadeTransition.setFromValue(0.0);
                                fadeTransition.setToValue(1.0);
                                fadeTransition.play();
                        }
                        ExecutionController executionController = loader.getController();
                        dynamicDisplay.getChildren().clear();
                        dynamicDisplay.getChildren().add(root);

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
                        case "SimulationName":
                                return this.simulationName;
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

        public void shutdownExecutorService() {
//                Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
//                        for (Thread thread : threadMap.keySet()) {
//                                thread.interrupt();
//                        }
                if(this.updateQueuePoolTask != null)
                        this.updateQueuePoolTask.cancel();
                this.engine.shutdownExecutorService();
        }
}

