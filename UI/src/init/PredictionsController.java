package init;

import DTO.*;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PredictionsController implements Initializable {
        private Stage primaryStage;

        private Engine engine;

        @FXML
        private Label fileLoadedLabel;

        private String simulationName;

        @FXML
        private StackPane dynamicDisplay;

        public void setEngine(Engine engine) {
                this.engine = engine;
        }
        @Override
        public void initialize(URL url, ResourceBundle rb){
        }

        @FXML
        protected void loadFile(ActionEvent event){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Simulation File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                this.simulationName = selectedFile.getName();
                if (selectedFile == null) {
                        return;
                }
                try {
                        this.engine.loadSimulationParametersFromFile(selectedFile.getAbsolutePath());
                        fileLoadedLabel.setText("Current File Loaded: " + selectedFile.getAbsolutePath());
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        private void setBooleanEnvProperty(PropertyDTO propertyDTO) {
                List<String> options = Arrays.asList("true", "false");

                ChoiceDialog<String> dialog = new ChoiceDialog<>(propertyDTO.getValue(), options);
                dialog.setTitle("Edit Environment Variable");
                dialog.setHeaderText(propertyDTO.getName() + " (" + propertyDTO.getType() + ")");
                dialog.setContentText("Choose a new value:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newPropertyValue -> {
                        try {
                                PropertyDTO tmp = new PropertyDTO(propertyDTO.getRange(), propertyDTO.getName(), propertyDTO.getType(), result.toString(), propertyDTO.isRandomlyGenerated());
                                engine.setEnvVariableWithDTO(propertyDTO);
                        } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(e.toString());
                                alert.showAndWait();
                        }
                });
        }

        private void setEnvProperty(PropertyDTO propertyDTO) {

                TextInputDialog dialog = new TextInputDialog(propertyDTO.getValue());
                dialog.setTitle("Edit Environment Variable");
                dialog.setHeaderText(propertyDTO.getName() + " (" + propertyDTO.getType() + ")");
                String text = (propertyDTO.getType().equals("String")) ? "enter a new value:" : "Enter a new value within the range [" + propertyDTO.getRange().toString() + "]:";
                dialog.setContentText(text);

                // Show the dialog and wait for user input
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newPropertyValue -> {
                        try {
                                PropertyDTO tmp = new PropertyDTO(propertyDTO.getRange(), propertyDTO.getName(), propertyDTO.getType(), newPropertyValue, propertyDTO.isRandomlyGenerated());
                                engine.setEnvVariableWithDTO(tmp);
                        } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(e.toString());
                                alert.showAndWait();
                                // throw new RuntimeException(e); // todo handle exception
                        }
                });
        }

        private void populateEntitiesPopulations() {
                ListView<EntityDTO> listView = new ListView<>();
                WorldDTO dto = engine.getSimulationParameters();

                // Populate the ListView with entities and their populations
                ObservableList<EntityDTO> entitiesWithPopulations = FXCollections.observableArrayList(dto.getEntities());
                listView.setItems(entitiesWithPopulations);

                // Customize the cell factory to display entity names and populations
                listView.setCellFactory(param -> new ListCell<EntityDTO>() {
                        @Override
                        protected void updateItem(EntityDTO entity, boolean empty) {
                                super.updateItem(entity, empty);
                                if (empty || entity == null) {
                                        setText(null);
                                } else {
                                        setText(entity.getName() + " (Population: " + entity.getPopulation() + ")");
                                }
                        }
                });

                // Add the ListView to your UI
                dynamicDisplay.getChildren().add(listView);
        }

        @FXML
        protected void newExecution(ActionEvent event) {
                dynamicDisplay.getChildren().clear();
                ListView<PropertyDTO> listView = setupPropertyListView();
                dynamicDisplay.getChildren().add(listView);
                populateEnvProperiesListView(listView);

                // Add button to manage entity populations
                Button manageEntityPopulationsButton = new Button("Manage Entity Populations");
                manageEntityPopulationsButton.setOnAction(e -> populateEntitiesPopulations());
                dynamicDisplay.getChildren().add(manageEntityPopulationsButton);
        }

        private ListView<PropertyDTO> setupPropertyListView() {
                ListView<PropertyDTO> listView = new ListView<>();
                listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                return listView;
        }

        private void populateEnvProperiesListView(ListView<PropertyDTO> listView) {
                WorldDTO dto = engine.getSimulationParameters();
                ObservableList<PropertyDTO> environmentProperties = FXCollections.observableArrayList(dto.getEnvironmentProperties());
                listView.setItems(environmentProperties);
                setupListViewCellFactory(listView);
                setupListViewSelectionListener(listView);
        }

        private void setupListViewCellFactory(ListView<PropertyDTO> listView) {
                listView.setCellFactory(param -> new ListCell<PropertyDTO>() {
                        @Override
                        protected void updateItem(PropertyDTO property, boolean empty) {
                                super.updateItem(property, empty);
                                if (empty || property == null) {
                                        setText(null);
                                } else {
                                        String range = (property.getRange() != null) ? property.getRange().toString() : "None";
                                        setText(property.getName() + " (" + property.getType() + ") Range: " + range + " Value: " + property.getValue());
                                }
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


        @FXML
        private void showEntityPopulationDialog() {
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Change Entity Populations");
                dialog.setHeaderText("Select an entity to change its population:");

                WorldDTO dto = engine.getSimulationParameters();

                VBox content = new VBox();
                for (EntityDTO entity : dto.getEntities()) {
                        HBox entityBox = new HBox();
                        Label nameLabel = new Label(entity.getName());
                        Label populationLabel = new Label("Population: " + entity.getPopulation());
                        Button changeButton = new Button("Change");

                        changeButton.setOnAction(e -> {
                                int newPopulation = showPopulationChangeDialog(entity.getName(), entity.getPopulation());
                                if (newPopulation >= 0) { // todo
                                        entity.setPopulation(newPopulation);
                                        populationLabel.setText("Population: " + entity.getPopulation());
                                }
                        });

                        entityBox.getChildren().addAll(nameLabel, populationLabel, changeButton);
                        content.getChildren().add(entityBox);
                }

                ScrollPane scrollPane = new ScrollPane(content);
                dialog.getDialogPane().setContent(scrollPane);

                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(closeButton);

                dialog.showAndWait();
        }

        private int showPopulationChangeDialog(String entityName, int currentPopulation) {
                TextInputDialog dialog = new TextInputDialog(String.valueOf(currentPopulation));
                dialog.setTitle("Change Entity Population");
                dialog.setHeaderText("Enter the new population for " + entityName + ":");
                dialog.setContentText("Current population: " + currentPopulation);

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                        try {
                                int newPopulation = Integer.parseInt(result.get());
                                if (newPopulation >= 0) {
                                        return newPopulation;
                                } else {
                                        // Handle negative input
                                }
                        } catch (NumberFormatException e) {
                                // Handle invalid input (non-numeric)
                        }
                }
                return -1; // Return a value indicating no change or invalid input
        }


        public void setPrimaryStage(Stage primaryStage) {
                this.primaryStage = primaryStage;
        }

        private TreeView<String> createAndSetupTreeView() {
                TreeView<String> parametersTreeView = new TreeView<>();
                parametersTreeView.setRoot(new TreeItem<>(simulationName));
                return parametersTreeView;
        }

        @FXML
        protected void showSimulationDetails(ActionEvent event) {
                dynamicDisplay.getChildren().clear();
                TreeView<String> parametersTreeView = createAndSetupTreeView();
                dynamicDisplay.getChildren().add(parametersTreeView);
                populateTreeView(parametersTreeView);
        }

        private void populateTreeView(TreeView<String> parametersTreeView) {
                WorldDTO dto = engine.getSimulationParameters();
                TreeItem<String> root = parametersTreeView.getRoot();
                TreeItem<String> enviromentVariables = new TreeItem<>("Environment Variables");
                TreeItem<String> rules = new TreeItem<>("Rules");
                TreeItem<String> entities = new TreeItem<>("Entities");
                TreeItem<String> termination = new TreeItem<>("Termination");

                root.getChildren().addAll(enviromentVariables, rules, entities, termination);

                populateEnvironmentProperties(dto.getEnvironmentProperties(), enviromentVariables);
                populateEntities(dto.getEntities(), entities);
                populateRules(dto.getRules(), rules);
                populateTermination(dto.getTermination(), termination);

                parametersTreeView.visibleProperty().set(true);
        }

        private void populateEnvironmentProperties(List<PropertyDTO> properties, TreeItem<String> parentItem) {
                for (PropertyDTO property : properties) {
                        TreeItem<String> propertyItem = new TreeItem<>(property.getName());
                        parentItem.getChildren().add(propertyItem);
                }
        }

        private void populateEntities(List<EntityDTO> entities, TreeItem<String> parentItem) {
                for (EntityDTO entity : entities) {
                        TreeItem<String> entityItem = new TreeItem<>(entity.getName());
                        TreeItem<String> populationItem = new TreeItem<>("Population: " + entity.getPopulation());
                        entityItem.getChildren().add(populationItem);
                        // ... Populate entity properties ...
                        parentItem.getChildren().add(entityItem);
                }
        }

        private void populateRules(List<RuleDTO> rules, TreeItem<String> parentItem) {
                for (RuleDTO rule : rules) {
                        TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
                        // ... Populate rule actions ...
                        parentItem.getChildren().add(ruleItem);
                }
        }

        private void populateTermination(TerminationDTO termination, TreeItem<String> parentItem) {
                if (termination.getTicks() != null) {
                        parentItem.getChildren().add(new TreeItem<>("Termination Ticks: " + termination.getTicks()));
                }
                if (termination.getSeconds() != null) {
                        parentItem.getChildren().add(new TreeItem<>("Termination Seconds: " + termination.getSeconds()));
                }
        }

        public void changeEnvVariables(ActionEvent actionEvent) {
        }

        public void manageEntityPopulations(ActionEvent actionEvent) {
        }
}

