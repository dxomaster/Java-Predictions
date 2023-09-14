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
                        List<RunEndDTO> runEndDTOS = engine.getPastArtifacts();
                        ListView<String> listView = new ListView<>();
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
                        dynamicDisplay.getChildren().clear();
                        dynamicDisplay.getChildren().add(listView);
                } catch (Exception e) {
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

        private void setBooleanEnvProperty(PropertyDTO propertyDTO) {
                List<String> options = Arrays.asList("true", "false");

                ChoiceDialog<String> dialog = new ChoiceDialog<>(propertyDTO.getValue(), options);
                dialog.setTitle("Edit Environment Variable");
                dialog.setHeaderText(propertyDTO.getName() + " (" + propertyDTO.getType() + ")");
                dialog.setContentText("Choose a new value:");

                modifyProperty(propertyDTO, dialog.showAndWait());
        }

        private void setEnvProperty(PropertyDTO propertyDTO) {

                TextInputDialog dialog = new TextInputDialog(propertyDTO.getValue());
                dialog.setTitle("Edit Environment Variable");
                dialog.setHeaderText(propertyDTO.getName() + " (" + propertyDTO.getType() + ")");
                String text = (propertyDTO.getType().equals("String")) ? "enter a new value:" : "Enter a new value within the range [" + propertyDTO.getRange().toString() + "]:";
                dialog.setContentText(text);

                modifyProperty(propertyDTO, dialog.showAndWait());
        }
        private void setPopulation(EntityDTO entityDTO) {

                TextInputDialog dialog = new TextInputDialog(String.valueOf(entityDTO.getPopulation()));
                dialog.setTitle("Edit Population");
                dialog.setHeaderText("Editing population for " + entityDTO.getName());
                String text = ("enter a new value: ");
                dialog.setContentText(text);

                modifyEntityDTO(entityDTO, dialog.showAndWait());
        }

        // Show the dialog and wait for user input
        private void modifyProperty(PropertyDTO propertyDTO, Optional<String> s) {
                Optional<String> result = s;
                result.ifPresent(newPropertyValue -> {
                        try {
                                PropertyDTO tmp = new PropertyDTO(propertyDTO.getRange(), propertyDTO.getName(), propertyDTO.getType(), newPropertyValue, propertyDTO.isRandomlyGenerated());
                                engine.setEnvVariableWithDTO(tmp);
                        } catch (Exception e) {
                                showErrorAlert(e);
                        }
                });
        }
        private void modifyEntityDTO(EntityDTO entityDTO, Optional<String> s) {
                Optional<String> result = s;
                result.ifPresent(newPropertyValue -> {
                        try {
                                engine.updateEntityPopulation(entityDTO.getName(), Integer.parseInt(newPropertyValue));
                        } catch (Exception e) {
                                showErrorAlert(e);
                        }
                });
        }

        public static void showErrorAlert(Exception e)
        {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
        }


        private ListView<EntityDTO> setupEntityPopulationsListView() {
                ListView<EntityDTO> entityPopulationListView = new ListView<>();
                entityPopulationListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                entityPopulationListView.setPrefWidth(250);
                List<EntityDTO> entities = engine.getSimulationParameters().getEntities();

                ObservableList<EntityDTO> observableEntities = FXCollections.observableArrayList(entities);
                entityPopulationListView.setItems(observableEntities);
                entityPopulationListView.setCellFactory(param -> new ListCell<EntityDTO>() {
                        @Override
                        protected void updateItem(EntityDTO entity, boolean empty) {
                                super.updateItem(entity, empty);
                                if (empty || entity == null)
                                        setGraphic(null);
                                else
                                        setText(entity.getName() + " Population: (" + entity.getPopulation() + ")");
                        }
                });
                setupListViewSelectionListenerPopulation(entityPopulationListView);

                return entityPopulationListView;
        }
        private void setupListViewSelectionListenerPopulation(ListView<EntityDTO> listView) {
                listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                                setPopulation(newValue);
                                newExecution(null);
                        }
                });
        }
        @FXML
        protected void newExecution(ActionEvent event) {
                try {
                        dynamicDisplay.getChildren().clear();

                        ListView<PropertyDTO> envVariablesDisplay = setupPropertyListView();
                        ListView<EntityDTO> entityPopulationView = setupEntityPopulationsListView();

                        dynamicDisplay.getChildren().addAll(envVariablesDisplay, entityPopulationView);

                        Button runButton = new Button("Run Simulation");
                        runButton.setOnAction(e -> {
                                try {
                                        engine.runSimulation();
                                        viewResults(null);
                                } catch (Exception ex) {
                                        showErrorAlert(ex);
                                }
                        });
                        dynamicDisplay.getChildren().add(runButton);
                }
                catch (Exception e)
                {
                        showErrorAlert(e);
                }
        }

        private ListView<PropertyDTO> setupPropertyListView() {
                ListView<PropertyDTO> listView = new ListView<>();
                listView.setPrefWidth(650);
                listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                WorldDTO dto = engine.getSimulationParameters();
                ObservableList<PropertyDTO> environmentProperties = FXCollections.observableArrayList(dto.getEnvironmentProperties());
                listView.setItems(environmentProperties);
                setupListViewCellFactory(listView);
                setupListViewSelectionListener(listView);
                return listView;
        }


        private void setupListViewCellFactory(ListView<PropertyDTO> listView) {
                listView.setCellFactory(param -> new ListCell<PropertyDTO>() {
                        @Override
                        protected void updateItem(PropertyDTO property, boolean empty) {
                                super.updateItem(property, empty);
                                if (empty || property == null) {
                                        setGraphic(null);
                                } else {
                                        String range = (property.getRange() != null) ? property.getRange().toString() : "None";
                                        setText(property.getName() + " (" + property.getType() + ") Range: " + range + " Value: " + property.getValue());
                                }
                                double preferredHeight = listView.getItems().size() * 24 + 2;
                                listView.setPrefHeight(preferredHeight);
                        }
                });
        }



        private void setupListViewSelectionListener(ListView<PropertyDTO> listView) {
                listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                                if (newValue.getType().equals("Boolean")) {
                                        setBooleanEnvProperty(newValue);
                                } else {
                                        setEnvProperty(newValue);
                                }
                                newExecution(null);
                        }
                });
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


        @Override
        protected Object handleGetObject(String key) {
                switch (key)
                {
                        case "Engine":
                                return this.engine;
                        case "SimulationName":
                                return this.simulationName;
                        default:
                                return null;
                }
        }

        @Override
        public Enumeration<String> getKeys() {
                return null;
        }
}

