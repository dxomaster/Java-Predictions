package init;

import engine.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class PredictionsApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //load from fxml file
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Design.fxml"));
        Parent predictionsRoot = fxmlLoader.load();
        PredictionsController predictionsController = fxmlLoader.getController();
        predictionsController.setPrimaryStage(primaryStage);
        predictionsController.setEngine(new Engine());
        //set scene
        primaryStage.setScene(new javafx.scene.Scene(predictionsRoot,1500,1000));

        //set title
        primaryStage.setTitle("Predictions");
        primaryStage.setOnCloseRequest(event -> predictionsController.shutdownExecutorService()); // Add a close request handler
        primaryStage.show();
        primaryStage.show();
    }
}