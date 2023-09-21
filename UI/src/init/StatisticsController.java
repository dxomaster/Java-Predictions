package init;

import DTO.RunStatisticsDTO;
import DTO.StatisticEntityDTO;
import DTO.StatisticPropertyDTO;
import Exception.ERROR.ErrorException;
import engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private ComboBox entitySelection;
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

        propertyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            String selectedEntity = entitySelection.getSelectionModel().getSelectedItem().toString();
            if (selectedEntity != null) {
                for (StatisticEntityDTO statisticEntityDTO : finalWorldDTO.getEntityDefinitionDTOList()) {
                    if (statisticEntityDTO.getName().equals(selectedEntity) && newSelection != null) {
                        for (StatisticPropertyDTO statisticPropertyDTO : statisticEntityDTO.getPropertyDTOList()) {
                            if (statisticPropertyDTO.getName().equals(newSelection.getName())) {
                                showValueFrequency(statisticPropertyDTO);
                            }
                        }
                    }
                }
            }
        });
        properties.setVisible(false);
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

        Scene scene = new Scene(populationChart, 800, 600);
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
        String selected = entitySelection.getSelectionModel().getSelectedItem().toString();
        List<StatisticEntityDTO> statisticEntityDTOS = engine.getPastSimulationStatisticsDTO(UUID).getEntityDefinitionDTOList();
        if (selected != null) {
            for (StatisticEntityDTO statisticEntityDTO : statisticEntityDTOS) {
                if (statisticEntityDTO.getName().equals(selected)) {
                    propertyTable.getItems().clear();
                    for (StatisticPropertyDTO statisticPropertyDTO : statisticEntityDTO.getPropertyDTOList()) {
                        propertyTable.getItems().add(statisticPropertyDTO);
                    }

                    properties.setVisible(true);
                    propertyTable.setVisible(true);
                }
            }
        }
    }

    private void showValueFrequency(StatisticPropertyDTO statisticPropertyDTO) {
        Stage chartStage = new Stage();
        chartStage.setTitle(statisticPropertyDTO.getName() + " histogram");

        BarChart<String, Number> histogramBarChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), new javafx.scene.chart.NumberAxis());
        histogramBarChart.setTitle(statisticPropertyDTO.getName() + " histogram");
        histogramBarChart.getXAxis().setLabel("Value");
        histogramBarChart.getYAxis().setLabel("Frequency");

        // Create an ObservableList of data series
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();

        // Create a single data series
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Data Series");

        // Populate the data series from the Map
        for (Map.Entry<String, Integer> entry : statisticPropertyDTO.getValueFrequency().entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the data series to the chart data
        barChartData.add(dataSeries);

        // Set the data for the chart
        histogramBarChart.setData(barChartData);

        Scene scene = new Scene(histogramBarChart, 800, 600);
        chartStage.setScene(scene);
        chartStage.show();
    }
}
