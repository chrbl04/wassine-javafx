// LoginScaled.java — pure JavaFX, letterboxed scaling + "Sign up" row
// VM options (Run Configuration):
// --module-path "C:\Users\charbelk\JDFX\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml
// (Optional) add: --enable-native-access=javafx.graphics

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginScaled extends Application {

    // Base layout size (design size)
    private static final double BASE_W = 1200.0;
    private static final double BASE_H = 700.0;

    // ---------- Utilities ----------
    private static ImageView icon(String classpath, double w, double h) {
        var url = Objects.requireNonNull(
                LoginScaled.class.getResource(classpath),
                "Missing resource: " + classpath
        );
        var img = new Image(url.toExternalForm(), w, h, true, true);
        var iv = new ImageView(img);
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        iv.setPreserveRatio(true);
        return iv;
    }

    private static ImageView responsiveBackground(String classpath, Pane parent) {
        var url = Objects.requireNonNull(
                LoginScaled.class.getResource(classpath),
                "Missing resource: " + classpath
        );
        var img = new Image(url.toExternalForm());
        var iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.fitWidthProperty().bind(parent.widthProperty());
        iv.fitHeightProperty().bind(parent.heightProperty());
        return iv;
    }

    @Override
    public void start(Stage stage) {
        // Root (layers)
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        // Background + overlay
        ImageView bgView = responsiveBackground("/resources/images/system/bgs/login_bg.jpg", root);

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.45));

        // Build UI at base size
        BorderPane content = buildContent();
        content.setPrefSize(BASE_W, BASE_H);

        // Letterbox: scale + center so content never goes off-screen
        ChangeListener<Number> letterbox = (obs, o, n) -> {
            double w = root.getWidth();
            double h = root.getHeight();
            double s = Math.min(w / BASE_W, h / BASE_H); // uniform scale

            content.setScaleX(s);
            content.setScaleY(s);

            // center the scaled content inside root
            content.setTranslateX((w - (BASE_W * s)) / 2.0);
            content.setTranslateY((h - (BASE_H * s)) / 2.0);
        };
        root.widthProperty().addListener(letterbox);
        root.heightProperty().addListener(letterbox);

        root.getChildren().addAll(bgView, overlay, content);

        Scene scene = new Scene(root, BASE_W, BASE_H);
        stage.setTitle("Wassine — Sign in");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(520);
        stage.show();

        // trigger initial layout
        letterbox.changed(null, null, null);
    }

    private BorderPane buildContent() {
        // ===== Left column =====
        Label welcome1 = new Label("Welcome");
        welcome1.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 56;
            -fx-font-weight: 800;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 2);
        """);

        Label welcome2 = new Label("Back");
        welcome2.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 56;
            -fx-font-weight: 800;
            -fx-translate-y: -18;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 2);
        """);

        Label blurb = new Label(
                "It’s a long established fact that a reader will be distracted by the " +
                        "readable content of a page when looking at its layout."
        );
        blurb.setWrapText(true);
        blurb.setMaxWidth(420);
        blurb.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 14;");

        HBox socials = new HBox(12,
                icon("/resources/images/system/social_media/facebook.png", 28, 28),
                icon("/resources/images/system/social_media/instagram.png", 28, 28),
                icon("/resources/images/system/social_media/X.png", 28, 28),
                // your blue logo instead of YouTube
                icon("/resources/images/system/logos/Logo_blue.png", 32, 32)
        );
        socials.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(14, welcome1, welcome2, blurb, socials);
        left.setAlignment(Pos.TOP_LEFT);

        // ===== Right column (Sign-in) =====
        Label title = new Label("Sign in");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28; -fx-font-weight: 700; -fx-padding: 0 0 8 0;");

        Label emailLbl = new Label("Email Address");
        emailLbl.setStyle("-fx-text-fill: #f1f1f1; -fx-font-size: 12;");

        TextField email = new TextField();
        email.setPromptText("name@example.com");
        email.setStyle("""
            -fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.92);
            -fx-padding: 10 12; -fx-text-fill: #222;
        """);

        Label passLbl = new Label("Password");
        passLbl.setStyle("-fx-text-fill: #f1f1f1; -fx-font-size: 12;");

        PasswordField pass = new PasswordField();
        pass.setPromptText("••••••••");
        pass.setStyle("""
            -fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.92);
            -fx-padding: 10 12; -fx-text-fill: #222;
        """);

        // --- NEW: "Sign up" row (replaces Remember Me) ---
        HBox signupRow = new HBox(6);
        signupRow.setAlignment(Pos.CENTER_LEFT);
        Label noAcc = new Label("Don't have an account?");
        noAcc.setStyle("-fx-text-fill: #f1f1f1; -fx-font-size: 12;");
        Hyperlink signUp = new Hyperlink("Sign up");
        signUp.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, "Sign up flow coming soon.").show());
        signupRow.getChildren().addAll(noAcc, signUp);

        Button signIn = new Button("Sign in now");
        signIn.setMaxWidth(160);
        signIn.setStyle("""
            -fx-background-color: #ff6a2a; -fx-background-radius: 10;
            -fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 16;
        """);
        signIn.setOnAction(e -> {
            // Hook up to DB or router later
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Demo sign-in: " + email.getText());
            a.setHeaderText(null);
            a.showAndWait();
        });

        Hyperlink lost = new Hyperlink("Lost your password?");
        lost.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, "Reset flow coming soon.").show());

        Label tos = new Label("By clicking on “Sign in now” you agree to Terms of Service | Privacy Policy");
        tos.setWrapText(true);
        tos.setMaxWidth(420);
        tos.setStyle("-fx-text-fill: #e3e3e3; -fx-font-size: 11; -fx-opacity: 0.95;");

        VBox right = new VBox(
                10, title,
                emailLbl, email,
                passLbl, pass,
                signupRow,           // <— here
                signIn,
                lost,
                tos
        );
        right.setAlignment(Pos.TOP_LEFT);
        right.setMaxWidth(420);

        // ===== Place in BorderPane =====
        BorderPane content = new BorderPane();
        content.setLeft(left);
        content.setRight(right);

        BorderPane.setMargin(left, new Insets(30));
        BorderPane.setMargin(right, new Insets(30));

        return content;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
