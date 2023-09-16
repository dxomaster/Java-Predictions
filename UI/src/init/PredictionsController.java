package init;

import DTO.*;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.*;

public class PredictionsController extends ResourceBundle implements Initializable  {
        private Stage primaryStage;

        private Engine engine;

        @FXML
        private Label fileLoadedLabel;

        private String simulationName;

        @FXML
        private HBox dynamicDisplay;

        public void setEngine(Engine engine) {
                this.engine = engine;
        }
        @Override
        public void initialize(URL url, ResourceBundle rb){
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
                } catch (Exception e) {
                        showErrorAlert(e);
                }
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



        @FXML
        protected void showSimulationDetails(ActionEvent event) {
                try {
                        FXMLLoader loader = new FXMLLoader();
                        URL mainFXML = getClass().getResource("SimulationDetails.fxml");
                        loader.setLocation(mainFXML);
                        loader.setResources(this);
                        Parent root = loader.load();
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
                        default:
                                return null;
                }
        }

        @Override
        public Enumeration<String> getKeys() {
                return null;
        }
}

