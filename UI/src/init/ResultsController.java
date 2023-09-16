package init;

import DTO.RunEndDTO;
import DTO.RunStatisticsDTO;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
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
            gridPane.add(listView, 0, 0);

        } catch (Exception e) {
            showErrorAlert(e);
        }
    }
}
