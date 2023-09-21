package init;

import DTO.RunStatisticsDTO;
import DTO.StatisticEntityDTO;
import DTO.StatisticPropertyDTO;
import Exception.ERROR.ErrorException;
import engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class StatisticsController implements javafx.fxml.Initializable{
    private Engine engine;
    private String UUID;
    @FXML
    private TableView<StatisticPropertyDTO> propertyTable;
    @FXML
    private Button showPopulationButton;
    @FXML
    private Label properties;
    @FXML
    ComboBox<String> entitySelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
        this.UUID = (String) resources.getObject("UUID");
        if(engine.isSimulationRunning(UUID)){
            //showErrorAlert(new ErrorException("Simulation is still running"));
            return;
        }
        RunStatisticsDTO worldDTO = null;
        try {
            worldDTO = engine.getPastSimulationStatisticsDTO(UUID);
        } catch (ErrorException e) {
            showErrorAlert(e);
        }

        RunStatisticsDTO finalWorldDTO = worldDTO;
        showPopulationButton.setOnAction(event -> {
            showPopulationGraph(finalWorldDTO.getEntityDefinitionDTOList());
        });

        // populate entity list
        for (StatisticEntityDTO statisticEntityDTO : worldDTO.getEntityDefinitionDTOList()) {
            entitySelection.getItems().add(statisticEntityDTO.getName());
        }
    }

    private void showPopulationGraph(List<StatisticEntityDTO> entityDefinitionDTOList) {
        Stage chartStage = new Stage();
        chartStage.setTitle("Population Over Ticks");

        LineChart<String, Number> populationChart = new LineChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
        populationChart.setTitle("Population Over Ticks");
        populationChart.getXAxis().setLabel("Ticks");
        populationChart.getYAxis().setLabel("Population");

        for(StatisticEntityDTO statisticEntityDTO : entityDefinitionDTOList){
            addDataToChart(statisticEntityDTO, populationChart);
        }

        Scene scene = new Scene(populationChart, 800, 600); // Adjust width and height as needed
        chartStage.setScene(scene);

        chartStage.show();
    }

    private void addDataToChart(StatisticEntityDTO statisticEntityDTO, LineChart<String, Number> populationOverTimeChart) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(statisticEntityDTO.getName());

        // add initial population
        List<Integer> populationOverTime = new ArrayList<>();
        populationOverTime.add(statisticEntityDTO.getPopulation());
        populationOverTime.addAll(statisticEntityDTO.getPopulationOverTime());

        int stepSize = 10;
        // add population over time every stepSize ticks
        for (int i = 0; i < statisticEntityDTO.getPopulationOverTime().size(); i += stepSize) {
            series.getData().add(new XYChart.Data<>(Integer.toString(i), populationOverTime.get(i)));
        }

        populationOverTimeChart.getData().add(series);
    }

    public void entitySelection(ActionEvent actionEvent) throws ErrorException {
        String selected = entitySelection.getSelectionModel().getSelectedItem();
        if (selected != null) {
            for (StatisticEntityDTO statisticEntityDTO : engine.getPastSimulationStatisticsDTO(UUID).getEntityDefinitionDTOList()) {
                if (statisticEntityDTO.getName().equals(selected)) {
                    propertyTable.getItems().clear();
                    for (StatisticPropertyDTO statisticPropertyDTO : statisticEntityDTO.getPropertyDTOList()) {
                        propertyTable.getItems().add(statisticPropertyDTO);
                    }
                }
            }
            properties.setVisible(true);
            propertyTable.setVisible(true);
        }
    }
}
