package init;

import DTO.*;
import engine.Engine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {
    Engine engine;
    @FXML
    private GridPane detailsGrid;
    String simulationName;
    private TreeView<String> parametersTreeView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
        this.simulationName = (String) resources.getObject("SimulationName");
        detailsGrid.getChildren().clear();
        this.parametersTreeView = createAndSetupTreeView();
        parametersTreeView.setOnMouseClicked(event ->{
            //todo fix the annoying exception here
            try {
                TreeItem<String> selectedItem = parametersTreeView.getSelectionModel().getSelectedItem();
                if (!selectedItem.getValue().equals("Environment Variables") && !selectedItem.getValue().equals("Rules")
                        && !selectedItem.getValue().equals("Entities") && !selectedItem.getValue().contains(".xml")) {
                    if(selectedItem.getValue().equals("Termination"))
                    {
                        detailsGrid.getChildren().clear();
                        detailsGrid.add(parametersTreeView, 0, 0);
                        showExpandedTerminationDetails(selectedItem.getValue());
                        return;
                    }

                    switch (selectedItem.getParent().getValue()) {
                        case "Environment Variables":
                            detailsGrid.getChildren().clear();
                            detailsGrid.add(parametersTreeView, 0, 0);
                            showExpandedEnvironmentVariableDetails(selectedItem.getValue());
                            break;
                        case "Rules":
                            detailsGrid.getChildren().clear();
                            detailsGrid.add(parametersTreeView, 0, 0);
                            showExpandedRuleDetails(selectedItem.getValue());
                            break;
                        case "Entities":
                            detailsGrid.getChildren().clear();
                            detailsGrid.add(parametersTreeView, 0, 0);
                            showExpandedEntityDetails(selectedItem.getValue());
                            break;
                    }
                }
                else {
                    detailsGrid.getChildren().clear();
                    detailsGrid.add(parametersTreeView, 0, 0);
                }
            }
            catch (Exception ignored)
            {

            }

        });
        detailsGrid.getChildren().add(parametersTreeView);
        populateTreeView(parametersTreeView);
    }

    private void showExpandedTerminationDetails(String value) {
        WorldDTO dto = engine.getSimulationParameters();
        TerminationDTO termination = dto.getTermination();
        ListView<String> listView = new ListView<>();
        if (termination.getTicks() != null) {
            listView.getItems().add("Termination by ticks: " + termination.getTicks());

        }
        if (termination.getSeconds() != null) {
            listView.getItems().add("Termination by seconds: " + termination.getSeconds());
        }
        detailsGrid.add(listView, 1, 0);
    }

    private void showExpandedEntityDetails(String value) {
        WorldDTO dto = engine.getSimulationParameters();
        ListView<String> tableView = new ListView<>();
        tableView.setOnMouseClicked(event -> {
            String selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem.contains("Property")) {
                String propertyName = selectedItem.split(":")[1].trim();
                String entityName = tableView.getItems().get(0).split(":")[1].trim();
                showExpandedPropertyDetails(entityName,propertyName);
            }
        });
        for (EntityDTO entity : dto.getEntities()) {
            if (entity.getName().equals(value)) {
                tableView.getItems().add("Name: " + entity.getName());
                for (PropertyDTO property : entity.getProperties()) {
                    tableView.getItems().add("Property Name: " + property.getName());
                }

            }

        }
        detailsGrid.add(tableView, 1, 0);
    }

    private void showExpandedPropertyDetails(String entityName, String propertyName) {
        WorldDTO dto = engine.getSimulationParameters();
        List<PropertyDTO> properties = null;
        ListView<String> tableView = new ListView<>();
        for (EntityDTO entityDTO : dto.getEntities())
        {
            if(entityDTO.getName().equals(entityName))
            {
                properties = entityDTO.getProperties();
            }
        }
        for(PropertyDTO propertyDTO : properties)
        {
            if(propertyDTO.getName().equals(propertyName))
            {
                tableView.getItems().add("Type: " + propertyDTO.getType());
                tableView.getItems().add("Is randomly initialized: " + propertyDTO.isRandomlyGenerated());
                if(propertyDTO.getRange() != null)
                {
                    tableView.getItems().add("Range: " + propertyDTO.getRange());
                }
            }
        }
        detailsGrid.add(tableView, 2, 0);
    }

    private void showExpandedRuleDetails(String value) {
        WorldDTO dto = engine.getSimulationParameters();
        for (RuleDTO rule : dto.getRules())
        {
            if(rule.getName().equals(value))
            {
                ListView<String> tableView = new ListView<>();
                tableView.getItems().add("Name: " + rule.getName());
                tableView.getItems().add("Probability: " + rule.getProbability());
                tableView.getItems().add("Ticks: " + rule.getTicks());
                tableView.setOnMouseClicked(event -> {
                    String selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem.contains("Action")) {
                        int selectedActionIndex = tableView.getSelectionModel().getSelectedIndex() - 3;// -3 because of the 3 first items
                        showExpandedActionDetails(rule.getActions().get(selectedActionIndex));


                    }
                });
                for(ActionableDTO action : rule.getActions())
                {
                    tableView.getItems().add("Action: " + action.getName());
                }
                detailsGrid.add(tableView, 1, 0);
            }
        }

    }

    private void showExpandedActionDetails(ActionableDTO actionableDTO) {
        ListView<String> tableView = new ListView<>();
        switch(actionableDTO.getName().toLowerCase())
        {
            case "calculation":
                ActionDTO calc = (ActionDTO) actionableDTO;
                tableView.getItems().add("Operator: " + calc.getOperator());
            case "decrease":
            case "increase":
            case "set":
                ActionDTO action = (ActionDTO) actionableDTO;
                tableView.getItems().add("Entity Name: " + action.getEntityName());
                tableView.getItems().add(Arrays.toString(action.getExpressions()));
                tableView.getItems().add("Property Name: " + action.getPropertyName());
                break;
            case "condition":
                ConditionDTO condition = (ConditionDTO) actionableDTO;
                SimpleConditionDTO simpleCondition = condition.getSimpleCondition();
                tableView.getItems().add("Entity Name: " + simpleCondition.getEntityName());
                tableView.getItems().add("Left Expression: " + simpleCondition.getPropertyName());
                tableView.getItems().add("Operator: " + simpleCondition.getOperator());
                tableView.getItems().add("Right Expression: " + simpleCondition.getExpression());
                tableView.getItems().add("Number of actions to perform if satisfied: " + condition.getActionsToPerformIfSatisfied().size());
                tableView.getItems().add("Number of actions to perform if not satisfied: " + condition.getActionsToPerformIfNotSatisfied().size());
                break;
            case "multiple condition":
                MultipleConditionDTO multipleCondition = (MultipleConditionDTO) actionableDTO;
                tableView.getItems().add("Operator: " + multipleCondition.getOperator());
                tableView.getItems().add("Number of conditions: " + multipleCondition.getConditions().size());
                tableView.getItems().add("Number of actions to perform if satisfied: " + multipleCondition.getActionsToPerformIfSatisfied().size());
                tableView.getItems().add("Number of actions to perform if not satisfied: " + multipleCondition.getActionsToPerformIfNotSatisfied().size());
                break;
            case "proximity":
                ProximityDTO proximity = (ProximityDTO) actionableDTO;
                tableView.getItems().add("Source Entity Name: " + proximity.getSourceEntityName());
                tableView.getItems().add("Target Entity Name: " + proximity.getTargetEntityName());
                tableView.getItems().add("Depth: " + proximity.getDepth());
                tableView.getItems().add("Number of actions to perform if near: " + proximity.getNumberOfActions());
                break;
            case "replace":
                ReplaceDTO replace = (ReplaceDTO) actionableDTO;
                tableView.getItems().add("Entity to kill: " + replace.getEntityToKill());
                tableView.getItems().add("Entity to create: " + replace.getEntityToCreate());
                tableView.getItems().add("Mode: " + replace.getMode());
                break;
            case "kill":
                ActionDTO kill = (ActionDTO) actionableDTO;
                tableView.getItems().add("Entity to kill: " + kill.getEntityName());
                break;
               // todo continue here




        }
        tableView.getItems().add("Secondary Entity Name: " + actionableDTO.getSecondaryEntityName());
        detailsGrid.add(tableView, 2, 0);


    }


    private void showExpandedEnvironmentVariableDetails(String variableName)
    {
        WorldDTO dto = engine.getSimulationParameters();
        for (PropertyDTO property : dto.getEnvironmentProperties()) {
            if (property.getName().equals(variableName)) {
                ListView<String> tableView = new ListView<>();
                tableView.getItems().add("Name: " + property.getName());
                tableView.getItems().add("Type: " + property.getType());
                tableView.getItems().add("Value: " + property.getValue());
                tableView.getItems().add("Range: " + (property.getRange() != null ? property.getRange().toString() : "None"));
                tableView.getItems().add("Random Initialization: " + property.isRandomlyGenerated());

                detailsGrid.add(tableView, 1, 0);

            }
        }
    }

    private TreeView<String> createAndSetupTreeView() {
        TreeView<String> parametersTreeView = new TreeView<>();
        parametersTreeView.setRoot(new TreeItem<>(simulationName));
        return parametersTreeView;
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
        parametersTreeView.visibleProperty().set(true);
    }

    private void populateProperties(List<PropertyDTO> properties, TreeItem<String> parentItem) {
        for (PropertyDTO property : properties) {
            TreeItem<String> propertyItem = new TreeItem<>(property.getName());
            parentItem.getChildren().add(propertyItem);
        }
    }
    private void populateEntities(List<EntityDTO> entities, TreeItem<String> parentItem) {
        for (EntityDTO entity : entities) {
            TreeItem<String> entityItem = new TreeItem<>(entity.getName());
            parentItem.getChildren().add(entityItem);
        }
    }

    private void populateRules(List<RuleDTO> rules, TreeItem<String> parentItem) {
        for (RuleDTO rule : rules) {
            TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
            parentItem.getChildren().add(ruleItem);
        }
    }




}
