import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TestFx extends Application {

    // helper: load image from resources
    private ImageView icon(String path, double w, double h) {
        Image img = new Image(getClass().getResourceAsStream(path), w, h, true, true);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        iv.setPreserveRatio(true);
        return iv;
    }

    @Override
    public void start(Stage stage) {
        // ===== Background =====
        Image bg = new Image(
                getClass().getResourceAsStream("/resources/images/system/bgs/login_bg.jpg"),
                1200, 700, false, true
        );
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(1200);
        bgView.setFitHeight(700);

        // dark overlay
        Rectangle overlay = new Rectangle(1200, 700);
        overlay.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 0, 0, 0.2)),
                new Stop(1, Color.rgb(0, 0, 0, 0.55))
        ));

        // ===== Left side: “Welcome Back”, blurb, social =====
        Label welcome1 = new Label("Welcome");
        welcome1.setStyle("-fx-text-fill: white; -fx-font-size: 56; -fx-font-weight: 800;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 2);");

        Label welcome2 = new Label("Back");
        welcome2.setStyle("-fx-text-fill: white; -fx-font-size: 56; -fx-font-weight: 800;"
                + "-fx-translate-y: -18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.2, 0, 2);");

        Label blurb = new Label(
                "It’s a long established fact that a reader will be distracted by the "
                        + "readable content of a page when looking at its layout."
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

        // ===== Right side: Sign-in form =====
        Label title = new Label("Sign in");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28; -fx-font-weight: 700; -fx-padding: 0 0 8 0;");

        Label emailLbl = new Label("Email Address"); emailLbl.setStyle("-fx-text-fill: #f1f1f1; -fx-font-size: 12;");
        TextField emailField = new TextField(); emailField.setPromptText("name@example.com");
        emailField.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.92);"
                + "-fx-padding: 10 12; -fx-text-fill: #222;");

        Label passLbl = new Label("Password"); passLbl.setStyle("-fx-text-fill: #f1f1f1; -fx-font-size: 12;");
        PasswordField passField = new PasswordField(); passField.setPromptText("••••••••");
        passField.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(255,255,255,0.92);"
                + "-fx-padding: 10 12; -fx-text-fill: #222;");

        CheckBox remember = new CheckBox("Remember Me");

        Button signIn = new Button("Sign in now");
        signIn.setMaxWidth(160);
        signIn.setStyle("-fx-background-color: #ff6a2a; -fx-background-radius: 10;"
                + "-fx-text-fill: white; -fx-font-weight: 700; -fx-padding: 10 16;");
        signIn.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Demo: email=" + emailField.getText(), ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
        });

        Hyperlink lost = new Hyperlink("Lost your password?");
        lost.setOnAction(e -> new Alert(Alert.AlertType.INFORMATION, "Reset flow TBD").show());

        Label tos = new Label("By clicking on “Sign in now” you agree to Terms of Service | Privacy Policy");
        tos.setWrapText(true);
        tos.setMaxWidth(420);
        tos.setStyle("-fx-text-fill: #e3e3e3; -fx-font-size: 11; -fx-opacity: 0.95;");

        VBox right = new VBox(10, title, emailLbl, emailField, passLbl, passField,
                new HBox(8, remember), signIn, lost, tos);
        right.setMaxWidth(420);
        right.setAlignment(Pos.TOP_LEFT);

        // ===== Place both sides in a BorderPane =====
        BorderPane content = new BorderPane();
        content.setLeft(left);
        content.setRight(right);
        BorderPane.setMargin(left, new Insets(30, 30, 30, 30));
        BorderPane.setMargin(right, new Insets(30, 30, 30, 30));

        // ===== Root =====
        StackPane root = new StackPane(bgView, overlay, content);
        root.setStyle("-fx-font-family: 'Segoe UI', Roboto, Arial, sans-serif;");

        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("Wassine — Sign in");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
