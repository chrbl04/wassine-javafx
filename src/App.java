import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.SceneLoader;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        stage.getIcons().add(
                new Image(getClass().getResourceAsStream(
                        "/resources/images/system/logos/Logo_blue.png"
                ))
        );

        stage.setTitle("Wassine");
        stage.setMinWidth(900);
        stage.setMinHeight(520);

        SceneLoader.init(stage);
        SceneLoader.load("/resources/ui/Login.fxml");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
