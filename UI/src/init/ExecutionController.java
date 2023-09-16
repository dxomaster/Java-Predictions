package init;

import DTO.EntityDTO;
import DTO.PropertyDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class ExecutionController implements Initializable {
    Engine engine;
    @FXML
    GridPane gridPane;
    ListView<PropertyDTO> envVariablesDisplay;
    ListView<EntityDTO> entityPopulationListView;
    Button runButton;
    ResourceBundle resources;



    private void setupListViewSelectionListenerPopulation(ListView<EntityDTO> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setPopulation(newValue);
              refreshScreen();
            }
        });
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

    private void moveToResultsScreen()
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("Results.fxml");
            loader.setLocation(mainFXML);
            loader.setResources(resources);
            Parent root = loader.load();
            ResultsController resultsController = loader.getController();
            HBox dynamicDisplay = (HBox)resources.getObject("dynamicDisplay");
            dynamicDisplay.getChildren().clear();
            dynamicDisplay.getChildren().add(root);
        }
        catch (Exception e)
        {
            showErrorAlert(e);
        }
    }
    private void setupListViewSelectionListener(ListView<PropertyDTO> listView) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getType().equals("Boolean")) {
                    setBooleanEnvProperty(newValue);
                } else {
                    setEnvProperty(newValue);
                }
               refreshScreen();
            }
        });
    }
    private void refreshScreen()
    {
        this.gridPane.getChildren().clear();
        this.entityPopulationListView = setupEntityPopulationsListView();
        this.envVariablesDisplay = setupPropertyListView();
        this.gridPane.add(envVariablesDisplay,0,0);
        this.gridPane.add(entityPopulationListView,1,0);
        gridPane.add(runButton,3,0);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.engine = (Engine) resources.getObject("Engine");
        ListView<PropertyDTO> envVariablesDisplay = setupPropertyListView();
        ListView<EntityDTO> entityPopulationView = setupEntityPopulationsListView();
        gridPane.add(envVariablesDisplay,0,0);
        gridPane.add(entityPopulationView,1,0);

        this.runButton = new Button("Run Simulation");
        runButton.setOnAction(e -> {
            try {
                engine.runSimulation();
                moveToResultsScreen();
            } catch (Exception ex) {
                showErrorAlert(ex);
            }
        });
        gridPane.add(runButton,3,0);

    }
}
