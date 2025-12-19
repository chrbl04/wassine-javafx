package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SceneLoader {

    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void load(String fxmlPath) {
        try {
            if (stage == null) throw new IllegalStateException("SceneLoader.init(stage) was not called");

            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root, 1200, 700);
            } else {
                scene.setRoot(root);
            }

            // keep same stylesheet always
            var css = SceneLoader.class.getResource("/resources/ui/application.css");
            if (css != null && scene.getStylesheets().isEmpty()) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "Failed to load: " + fxmlPath + "\n\n" + e.getClass().getSimpleName() + ": " + e.getMessage());
            a.setHeaderText("Scene Load Error");
            a.showAndWait();
        }
    }
}
