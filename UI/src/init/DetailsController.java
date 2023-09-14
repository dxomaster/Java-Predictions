package init;

import DTO.*;
import engine.Engine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import rule.Rule;
import world.World;

import java.net.URL;
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
            TreeItem<String> selectedItem = parametersTreeView.getSelectionModel().getSelectedItem();
            if(!selectedItem.getValue().equals("Environment Variables") && !selectedItem.getValue().equals("Rules")
                && !selectedItem.getValue().equals( "Entities") && !selectedItem.getValue().equals("Termination"))
            {
                switch(selectedItem.getParent().getValue())
                {
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
                        //detailsGrid.getChildren().clear();
                       // showExpandedEntityDetails(selectedItem.getValue());
                        break;
                    case "Termination":
                        //detailsGrid.getChildren().clear();
                     //   showExpandedTerminationDetails(selectedItem.getValue());
                        break;
                }
            }

        });
        detailsGrid.getChildren().add(parametersTreeView);
        populateTreeView(parametersTreeView);
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
                        int selectedActionIndex = tableView.getSelectionModel().getSelectedIndex() - 3;
                        System.out.println("Selected action index: " + selectedActionIndex);
                        System.out.println("Selected item: " + selectedItem);
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
        switch(actionableDTO.getName())
        {
            case "increase":
                ActionDTO action = (ActionDTO) actionableDTO;
                tableView.getItems().add("Name: " + action.getName());
                tableView.getItems().add("Entity Name: " + action.getEntityName());
               // todo continue here

        }


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
            TreeItem<String> activationItem = new TreeItem<>("Activation");
            activationItem.getChildren().add(new TreeItem<>("Ticks: " + rule.getTicks()));
            activationItem.getChildren().add(new TreeItem<>("Probability: " + rule.getProbability()));
            ruleItem.getChildren().add(activationItem);
            populateActions(rule.getActions(), ruleItem);
            parentItem.getChildren().add(ruleItem);
        }
    }
    private void populateActions(List<ActionableDTO> actions, TreeItem<String> parentItem) {
        for (ActionableDTO action : actions) {
            TreeItem<String> actionItem = createActionItem(action);
            parentItem.getChildren().add(actionItem);
        }
    }

    private TreeItem<String> createActionItem(ActionableDTO action) {
        TreeItem<String> actionItem = new TreeItem<>("Action type: " + action.getName());

        if (action instanceof ConditionDTO) {
            populateConditionAction((ConditionDTO) action, actionItem);
        } else if (action instanceof MultipleConditionDTO) {
            populateMultipleConditionAction((MultipleConditionDTO) action, actionItem);
        } else if (action instanceof ActionDTO) {
            populateActionDTO((ActionDTO) action, actionItem);
        } else if (action instanceof ProximityDTO) {
            populateProximityDTO((ProximityDTO) action, actionItem);
        } else if (action instanceof ReplaceDTO) {
            populateReplaceDTO((ReplaceDTO) action, actionItem);
        }

        return actionItem;
    }

    private void populateReplaceDTO(ReplaceDTO action, TreeItem<String> actionItem) {
        //todo imeplement
    }

    private void populateProximityDTO(ProximityDTO action, TreeItem<String> actionItem) {
        //todo imeplement
    }

    private void populateMultipleConditionAction(MultipleConditionDTO condition, TreeItem<String> actionItem) {
        TreeItem<String> conditionAmount = new TreeItem<>("Number of conditions: "+ condition.getConditions().size());
        TreeItem<String> ifActionAmount = new TreeItem<>("Number of actions to perform if satisfied: "+ condition.getActionsToPerformIfSatisfied().size());
        TreeItem<String> ifNotActionAmount = new TreeItem<>("Number of actions to perform if not satisfied: "+condition.getActionsToPerformIfNotSatisfied().size());
        TreeItem<String> logicOperator = new TreeItem<>("Logic Operator: "+condition.getOperator());
        actionItem.getChildren().addAll(conditionAmount,ifActionAmount, ifNotActionAmount, logicOperator);
    }

    private void populateConditionAction(ConditionDTO condition, TreeItem<String> actionItem) {
        TreeItem<String> ifActionAmount = new TreeItem<>("Number of actions to perform if satisfied: "+ condition.getActionsToPerformIfSatisfied().size());
        TreeItem<String> ifNotActionAmount = new TreeItem<>("Number of actions to perform if not satisfied: "+condition.getActionsToPerformIfNotSatisfied().size());
        TreeItem<String> property = new TreeItem<>("Property: "+condition.getSimpleCondition().getProperty());
        TreeItem<String> operator = new TreeItem<>("Operator: "+condition.getSimpleCondition().getOperator());
        TreeItem<String> value = new TreeItem<>("Value: "+condition.getSimpleCondition().getExpression());
        actionItem.getChildren().addAll(ifActionAmount, ifNotActionAmount, property, operator, value);
    }

    private void populateActionDTO(ActionDTO action, TreeItem<String> actionItem) {
        TreeItem<String> entity = new TreeItem<>("Entity: "+ action.getEntityName());
        if(action.getPropertyName() != null) {
            TreeItem<String> property = new TreeItem<>("Property: " + action.getPropertyName());
            actionItem.getChildren().add(property);
        }
        if(!action.getOperator().equals("none")) {
            TreeItem<String> operator = new TreeItem<>("Operator: " + action.getOperator());
            actionItem.getChildren().add(operator);
        }
        if(((ActionDTO) action).getExpressions().length != 0) {
            TreeItem<String> arguments = new TreeItem<>("Arguments: " + String.join(" ", action.getExpressions()));
            actionItem.getChildren().add(arguments);
        }

        actionItem.getChildren().addAll(entity);
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
