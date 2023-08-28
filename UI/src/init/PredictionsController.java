package init;

import DTO.*;
import engine.Engine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

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

        @FXML
        private TableView<EntityDTO> entityPopulationsTable;

        @FXML
        private TableColumn<EntityDTO, String> entityColumn;
        @FXML
        private TableColumn<EntityDTO, Integer> populationColumn;

        @FXML
        private HBox entityPopulationsContainer; // The layout container holding both ListView and TableView

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

        private void showErrorAlert(Exception e)
        {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
        }

        private void populateEntitiesPopulations() {
                WorldDTO dto = engine.getSimulationParameters();
                List<EntityDTO> entities = engine.getSimulationParameters().getEntities();

                // Populate the List with entities and their populations
                ObservableList<EntityDTO> observableEntities = FXCollections.observableArrayList(entities);
                entityPopulationsTable.setItems(observableEntities);
        }

        private void setupEntityPopulationsTableView() {
                entityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
                populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());

                // Customize the population column to allow editing
                populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                populationColumn.setOnEditCommit(event -> {
                        EntityDTO entity = event.getRowValue();
                        entity.setPopulation(event.getNewValue());
                        try { // Update in the engine
                                // todo change population only if possible using rows and columns given in the xml file
                                engine.updateEntityPopulation(entity.getName(), event.getNewValue());
                        } catch (Exception e) {
                                showErrorAlert(e);
                        }
                });

                entityPopulationsTable.setEditable(true);
                populateEntitiesPopulations();

                entityPopulationsTable.setPrefWidth(400);
        }

        @FXML
        protected void newExecution(ActionEvent event) {
                try {
                        dynamicDisplay.getChildren().clear();
                        ListView<PropertyDTO> listView = setupPropertyListView();
                        populateEnvPropertiesListView(listView);
                        listView.setPrefWidth(650);

                        // Clear existing child nodes from entityPopulationsContainer
                        entityPopulationsContainer.getChildren().clear();

                        // Setup and populate the entity populations TableView
                        setupEntityPopulationsTableView();

                        // Show the container holding both ListView and TableView
                        entityPopulationsContainer.setVisible(true);

                        // Add the ListView, TableView, and button to the entityPopulationsContainer
                        entityPopulationsContainer.getChildren().addAll(listView, entityPopulationsTable);

                        // Show the entityPopulationsContainer in the dynamicDisplay StackPane
                        dynamicDisplay.getChildren().add(entityPopulationsContainer);
                }
                catch (Exception e)
                {
                        showErrorAlert(e);
                }
        }

        private ListView<PropertyDTO> setupPropertyListView() {
                ListView<PropertyDTO> listView = new ListView<>();
                listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                return listView;
        }

        private void populateEnvPropertiesListView(ListView<PropertyDTO> listView) {
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
                                newExecution(null); //todo do we have to?
                        }
                });
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
                try {

                        dynamicDisplay.getChildren().clear();
                        TreeView<String> parametersTreeView = createAndSetupTreeView();
                        dynamicDisplay.getChildren().add(parametersTreeView);
                        populateTreeView(parametersTreeView);
                }
                catch (Exception e) {
                        showErrorAlert(e);
                }
        }

        private void populateTreeView(TreeView<String> parametersTreeView) {
                WorldDTO dto = engine.getSimulationParameters();
                TreeItem<String> root = parametersTreeView.getRoot();
                TreeItem<String> enviromentVariables = new TreeItem<>("Environment Variables");
                TreeItem<String> rules = new TreeItem<>("Rules");
                TreeItem<String> entities = new TreeItem<>("Entities");
                TreeItem<String> termination = new TreeItem<>("Termination");

                root.getChildren().addAll(enviromentVariables, rules, entities, termination);

                populateProperties(dto.getEnvironmentProperties(), enviromentVariables);
                populateEntities(dto.getEntities(), entities);
                populateRules(dto.getRules(), rules);
                populateTermination(dto.getTermination(), termination);

                parametersTreeView.visibleProperty().set(true);
        }

        private void populateProperties(List<PropertyDTO> properties, TreeItem<String> parentItem) {
                for (PropertyDTO property : properties) {
                        TreeItem<String> propertyItem = new TreeItem<>(property.getName());
                        TreeItem<String> valueItem = new TreeItem<>("Value: " + property.getValue());
                        TreeItem<String> rangeItem = new TreeItem<>("Range: " + (property.getRange() != null ? property.getRange().toString() : "None"));
                        TreeItem<String> typeItem = new TreeItem<>("Type: " + property.getType());
                        TreeItem<String> randomInitItem = new TreeItem<>("Random Initialization: " + property.isRandomlyGenerated());
                        propertyItem.getChildren().add(randomInitItem);
                        propertyItem.getChildren().add(typeItem);
                        propertyItem.getChildren().addAll(valueItem, rangeItem);
                        parentItem.getChildren().add(propertyItem);
                }
        }
        private void populateEntities(List<EntityDTO> entities, TreeItem<String> parentItem) {
                for (EntityDTO entity : entities) {
                        TreeItem<String> entityItem = new TreeItem<>(entity.getName());
                        TreeItem<String> populationItem = new TreeItem<>("Population: " + entity.getPopulation());
                        entityItem.getChildren().add(populationItem);
                        populateProperties(entity.getProperties(), entityItem);
                        parentItem.getChildren().add(entityItem);
                }
        }

        private void populateRules(List<RuleDTO> rules, TreeItem<String> parentItem) {
                for (RuleDTO rule : rules) {
                        TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
                        populateActions(rule.getActions(), ruleItem);
                        parentItem.getChildren().add(ruleItem);
                }
        }
        private void populateActions(List<ActionableDTO> actions, TreeItem<String> parentItem) {
                for (ActionableDTO action : actions) {
                        TreeItem<String> actionItem = new TreeItem<>("Action type: " + action.getName());
                        if (action instanceof ConditionDTO)
                        {
                                ConditionDTO condition = (ConditionDTO) action;
                                TreeItem<String> ifActionAmount = new TreeItem<>("Number of actions to perform if satisfied: "+ condition.getActionsToPerformIfSatisfied().size());
                                TreeItem<String> ifNotActionAmount = new TreeItem<>("Number of actions to perform if not satisfied: "+condition.getActionsToPerformIfNotSatisfied().size());
                                TreeItem<String> property = new TreeItem<>("Property: "+condition.getSimpleCondition().getProperty());
                                TreeItem<String> operator = new TreeItem<>("Operator: "+condition.getSimpleCondition().getOperator());
                                TreeItem<String> value = new TreeItem<>("Value: "+condition.getSimpleCondition().getExpression());
                                actionItem.getChildren().addAll(ifActionAmount, ifNotActionAmount, property, operator, value);

                        }
                        else if (action instanceof MultipleConditionDTO)
                        {
                                MultipleConditionDTO condition = (MultipleConditionDTO) action;
                                TreeItem<String> conditionAmount = new TreeItem<>("Number of conditions: "+ condition.getConditions().size());
                                TreeItem<String> ifActionAmount = new TreeItem<>("Number of actions to perform if satisfied: "+ condition.getActionsToPerformIfSatisfied().size());
                                TreeItem<String> ifNotActionAmount = new TreeItem<>("Number of actions to perform if not satisfied: "+condition.getActionsToPerformIfNotSatisfied().size());
                                TreeItem<String> logicOperator = new TreeItem<>("Logic Operator: "+condition.getOperator());
                                actionItem.getChildren().addAll(conditionAmount,ifActionAmount, ifNotActionAmount, logicOperator);

                        }
                        parentItem.getChildren().add(actionItem);
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


}

