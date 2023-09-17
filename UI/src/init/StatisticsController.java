package init;

import DTO.RunStatisticsDTO;
import DTO.StatisticEntityDTO;
import DTO.WorldDTO;
import Exception.ERROR.ErrorException;
import engine.Engine;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static init.PredictionsController.showErrorAlert;

public class StatisticsController implements javafx.fxml.Initializable{
    private Engine engine;
    private String UUID;
    @FXML
    LineChart<String, Number> populationOverTimeChart;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.engine = (Engine) resources.getObject("Engine");
        this.UUID = (String) resources.getObject("UUID");
        if(engine.isSimulationRunning(UUID)){
            showErrorAlert(new ErrorException("Simulation is still running"));
            return;
        }
        RunStatisticsDTO worldDTO = null;
        try {
            worldDTO = engine.getPastSimulationStatisticsDTO(UUID);
        } catch (ErrorException e) {
            showErrorAlert(e);
        }
        populationOverTimeChart.setTitle("Population over time");
        populationOverTimeChart.getXAxis().setLabel("Time");
        populationOverTimeChart.getYAxis().setLabel("Population");

        for(StatisticEntityDTO statisticEntityDTO : worldDTO.getEntityDefinitionDTOList()){
            addDataToChart(statisticEntityDTO);
        }


    }

    private void addDataToChart(StatisticEntityDTO statisticEntityDTO) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(statisticEntityDTO.getName());

        // Add data points from the list
        for (int i = 0; i < statisticEntityDTO.getPopulationOverTime().size() / 50; i++) {
            int yValue = statisticEntityDTO.getPopulationOverTime().get(i*50);
            series.getData().add(new XYChart.Data<>(String.valueOf(i*50 ), yValue)); // x-values are indices
        }
        populationOverTimeChart.getData().add(series);

        //populationOverTimeChart.getData().add(worldDTO.getPopulationOverTime());
    }
}
