package init;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CoolAnimation {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;

    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;
    private String currentImage = "westley.png";

    private ImageView imageView;
    private double xSpeed = 4; // Horizontal speed
    private double ySpeed = 4; // Vertical speed

    public void Animate() {
        Pane root = new Pane();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        Stage stage = new Stage();
        // Load an image
        Image image = new Image(String.valueOf(getClass().getResource(currentImage)));

        // Create an ImageView for the image
        imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);

        // Set initial position
        imageView.setLayoutX((SCENE_WIDTH - IMAGE_WIDTH) / 2);
        imageView.setLayoutY((SCENE_HEIGHT - IMAGE_HEIGHT) / 2);

        // Add the image to the scene
        root.getChildren().add(imageView);

        stage.setTitle("Cool Animation");
        stage.setScene(scene);
        stage.show();

        // Create a timeline for animation
        Duration duration = Duration.millis(16); // About 60 frames per second
        KeyFrame keyFrame = new KeyFrame(duration, e -> moveImage());
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(300);
        timeline.setOnFinished(event -> closeWindow(stage));
        timeline.play();
    }

    private void moveImage() {
        // Get current position
        double x = imageView.getLayoutX();
        double y = imageView.getLayoutY();

        // Update position
        x += xSpeed;
        y += ySpeed;

        // Check for collisions with screen boundaries
        if (x <= 0 || x + IMAGE_WIDTH >= SCENE_WIDTH) {
            xSpeed *= -1; // Reverse horizontal direction on collision
            switchImage();

        }
        if (y <= 0 || y + IMAGE_HEIGHT >= SCENE_HEIGHT) {
            ySpeed *= -1; // Reverse vertical direction on collision
            switchImage();
        }

        // Set the new position
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }
    private void switchImage()
    {
        if(this.currentImage.equals("westley.png"))
        {
            this.currentImage = "aviad.png";
        }
        else
        {
            this.currentImage = "westley.png";
        }
        Image image = new Image(String.valueOf(getClass().getResource(currentImage))); // Replace with your image file
        imageView.setImage(image);

    }
    private void closeWindow(Stage stage) {
        stage.close();
    }
}
