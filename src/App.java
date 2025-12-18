import javafx.application.Application;
import javafx.stage.Stage;
import util.SceneLoader;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        SceneLoader.init(stage);

        stage.setTitle("Wassine — Sign in");
        stage.setMinWidth(900);
        stage.setMinHeight(520);

        SceneLoader.load("/resources/ui/Login.fxml");

        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
