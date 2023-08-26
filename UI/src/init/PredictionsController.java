package init;

import DTO.*;
import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rule.action.Action;

import java.io.File;
import java.net.URL;
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
        protected void newExecution(ActionEvent event)
        {
                dynamicDisplay.getChildren().clear();
                ListView<String> listView = new ListView<>();
                listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                dynamicDisplay.getChildren().add(listView);
                WorldDTO dto = this.engine.getSimulationParameters();
                for (PropertyDTO environmentProperty : dto.getEnvironmentProperties()) {
                        listView.getItems().add(environmentProperty.getName() + " : " + environmentProperty.getValue());

                }
                listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                                System.out.println("Selected item: " + newValue);
                        }
                });
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
        public void setPrimaryStage(Stage primaryStage) {
                this.primaryStage = primaryStage;
        }

        @FXML
        protected void showSimulationDetails(ActionEvent event) {
                try {
                        dynamicDisplay.getChildren().clear();
                        TreeView<String> parametersTreeView = new TreeView<>();
                        dynamicDisplay.getChildren().add(parametersTreeView);
                        WorldDTO dto = this.engine.getSimulationParameters();
                        TreeItem<String> root = new TreeItem<>(simulationName);
                        parametersTreeView.setRoot(root);
                        TreeItem<String> enviromentVariables = new TreeItem<>("Enviroment Variables");
                        TreeItem<String> rules = new TreeItem<>("Rules");
                        TreeItem<String> entities = new TreeItem<>("Entities");
                        root.getChildren().addAll(enviromentVariables, rules, entities);
                        for (PropertyDTO property : dto.getEnvironmentProperties()) {
                                TreeItem<String> propertyItem = new TreeItem<>(property.getName());
                                enviromentVariables.getChildren().add(propertyItem);
                        }
                        for(RuleDTO rule: dto.getRules()){
                                TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
                                rules.getChildren().add(ruleItem);
                                for(ActionableDTO action : rule.getActions()){
                                        TreeItem<String> actionItem = new TreeItem<>(action.getName());
                                        if(action instanceof ConditionDTO)
                                        {

                                                for(ActionableDTO innerAction : ((ConditionDTO) action).getActionsToPerformIfSatisfied()){
                                                        TreeItem<String> actionItem2 = new TreeItem<>(innerAction.getName());
                                                        actionItem.getChildren().add(actionItem2);
                                                }
                                        }
                                        else
                                                ruleItem.getChildren().add(new TreeItem<>(action.getName()));

                                        ruleItem.getChildren().add(actionItem);
                                }
                        }

                        parametersTreeView.visibleProperty().set(true);
                }
                catch (Exception e){
                        e.printStackTrace();
                }

        }

}
