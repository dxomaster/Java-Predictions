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
        //set scene
        Parent p = fxmlLoader.load();
        primaryStage.setScene(new javafx.scene.Scene(p));

        //set title
        primaryStage.setTitle("Predictions");

        primaryStage.show();
    }
}
