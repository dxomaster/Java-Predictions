package init;

import DTO.RunEndDTO;
import DTO.RunStatisticsDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class ResultsController implements Initializable {
    Engine engine;
    @FXML
    GridPane gridPane;
    @FXML
    ComboBox<String> viewSelection;
    ListView<String> listView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
        viewSelection.getItems().addAll("Run Progress","Entity Information", "Statistics");

        try {
            List<RunEndDTO> runEndDTOS = engine.getPastArtifacts();
            listView = new ListView<>();
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
            GridPane.setConstraints(listView, 0, 0,1,3);
            gridPane.add(listView, 0, 0);
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String runUUID = listView.getSelectionModel().getSelectedItem().split("\n")[0].split(":")[1].trim();
                    viewRunProgress(runUUID);
                    System.out.println("Selected item: " + newValue);
                }
            });


        } catch (Exception e) {
            showErrorAlert(e);
        }
    }
    private void viewRunProgress(String runUUID){//todo here
        WorldDTO dto = engine.getWorldDTOByUUID(runUUID);
        IntegerProperty currentTick = engine.getCurrentTickPropertyByUUID(runUUID);
        while(true)
        {
            System.out.println(currentTick.get());
        }






    }
}
