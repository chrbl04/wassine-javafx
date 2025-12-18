package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public final class SceneLoader {

    private static Stage stage;

    private SceneLoader() {}

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void load(String fxmlPath) {
        if (stage == null) throw new IllegalStateException("SceneLoader not initialized. Call SceneLoader.init(stage) in App.start().");

        try {
            double w = stage.getScene() != null ? stage.getScene().getWidth() : 1200;
            double h = stage.getScene() != null ? stage.getScene().getHeight() : 700;

            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    SceneLoader.class.getResource(fxmlPath),
                    "FXML not found: " + fxmlPath
            ));

            Scene scene = new Scene(root, w, h);

            scene.getStylesheets().add(Objects.requireNonNull(
                    SceneLoader.class.getResource("/resources/ui/application.css"),
                    "CSS not found: /resources/ui/application.css"
            ).toExternalForm());

            stage.setScene(scene);
            stage.setWidth(w);
            stage.setHeight(h);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load scene: " + fxmlPath, e);
        }
    }
}
