package init;

import DTO.EntityDTO;
import DTO.WorldDTO;
import engine.Engine;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EntityInformationController implements Initializable {
    @FXML
    TableView<EntityDTO> entityInformationTable;
    TableColumn<EntityDTO, String> nameColumn;
    TableColumn<EntityDTO, String> populationColumn;
    private final Map<String, UpdateEntityInformationTask> tasks = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn = (TableColumn<EntityDTO, String>) entityInformationTable.getColumns().get(0);
        populationColumn = (TableColumn<EntityDTO, String>) entityInformationTable.getColumns().get(1);
        Engine engine = (Engine) resources.getObject("Engine");
        String UUID = (String) resources.getObject("UUID");
        WorldDTO worldDTO = engine.getWorldDTOByUUID(UUID);
        ObservableList<EntityDTO> entityDTOObservableList = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        populationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCurrentPopulation())));
        entityDTOObservableList.addAll(worldDTO.getEntities());
        entityInformationTable.setItems(entityDTOObservableList);
        if(!tasks.containsKey(UUID)){
            UpdateEntityInformationTask updateEntityInformationTask = new UpdateEntityInformationTask(engine, UUID, entityDTOObservableList);
            Thread thread = new Thread(updateEntityInformationTask);
            thread.start();
        }



    }
}
