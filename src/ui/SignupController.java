package ui;

import dao.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class SignupController {

    @FXML private GridPane root;
    @FXML private StackPane leftPane;
    @FXML private ImageView bgImage;
    @FXML private Rectangle overlay;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField countryCodeField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private TextField countryField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField buildingField;

    @FXML private TextField profileImageUrlField;
    @FXML private TextField idImageUrlField;

    private final UserDao userDao = new UserDao();

    @FXML
    private void initialize() {
        if (countryCodeField.getText() == null || countryCodeField.getText().isBlank()) {
            countryCodeField.setText("+961");
        }

        URL bgUrl = Objects.requireNonNull(
                getClass().getResource("/resources/images/system/bgs/login_bg.jpg"),
                "Background not found"
        );
        Image img = new Image(bgUrl.toExternalForm());
        bgImage.setImage(img);
        bgImage.setPreserveRatio(true);
        bgImage.setSmooth(true);

        Runnable cover = () -> {
            double paneW = Math.max(1, leftPane.getWidth());
            double paneH = Math.max(1, leftPane.getHeight());
            double imgW  = img.getWidth();
            double imgH  = img.getHeight();
            if (imgW <= 0 || imgH <= 0) return;

            double scale = Math.max(paneW / imgW, paneH / imgH);
            double fitW = imgW * scale;
            double fitH = imgH * scale;

            bgImage.setFitWidth(fitW);
            bgImage.setFitHeight(fitH);
            bgImage.setTranslateX((paneW - fitW) / 2.0);
            bgImage.setTranslateY((paneH - fitH) / 2.0);

            overlay.setWidth(paneW);
            overlay.setHeight(paneH);
        };

        leftPane.widthProperty().addListener((o,a,b) -> cover.run());
        leftPane.heightProperty().addListener((o,a,b) -> cover.run());
        if (img.getProgress() >= 1) cover.run();
        else img.progressProperty().addListener((o,a,b) -> { if (b.doubleValue() >= 1) cover.run(); });

        overlay.setMouseTransparent(true);
        overlay.setFill(new javafx.scene.paint.LinearGradient(
                0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                java.util.List.of(
                        new javafx.scene.paint.Stop(0.00, javafx.scene.paint.Color.web("#0a2a6a", 0.70)),
                        new javafx.scene.paint.Stop(0.55, javafx.scene.paint.Color.TRANSPARENT),
                        new javafx.scene.paint.Stop(1.00, javafx.scene.paint.Color.web("#5a3a18", 0.70))
                )
        ));
    }

    @FXML
    private void onCreateAccount() {
        if (firstNameField.getText().isBlank() ||
                lastNameField.getText().isBlank() ||
                emailField.getText().isBlank() ||
                phoneField.getText().isBlank() ||
                passwordField.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match.").show();
            return;
        }

        model.User u = new model.User();
        u.setFirstName(firstNameField.getText().trim());
        u.setLastName(lastNameField.getText().trim());
        u.setEmail(emailField.getText().trim());

        // status removed => pick a default your DB accepts
        u.setStatus("Active");

        u.setCountryCode(countryCodeField.getText().trim());
        u.setPhone(phoneField.getText().trim());

        // keep consistent with your current login approach
        u.setPasswordHash(passwordField.getText());

        u.setCountry(countryField.getText().trim());
        u.setCity(cityField.getText().trim());
        u.setStreet(streetField.getText().trim());
        u.setBuilding(buildingField.getText().trim());

        u.setProfileImageUrl(profileImageUrlField.getText().trim());
        u.setIdImageUrl(idImageUrlField.getText().trim());

        int newId = userDao.insert(u);

        if (newId > 0) {
            new Alert(Alert.AlertType.INFORMATION, "Account created! user_id = " + newId).showAndWait();
            goToLogin();
        } else {
            new Alert(Alert.AlertType.ERROR, "Signup failed.").show();
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();

            // preserve current window size
            double w = stage.getWidth();
            double h = stage.getHeight();

            Parent view = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/resources/ui/Login.fxml"),
                    "Login.fxml not found"
            ));

            Scene scene = new Scene(view, w, h);
            stage.setScene(scene);

            // keep exact size (prevents “dimensions messed up”)
            stage.setWidth(w);
            stage.setHeight(h);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Login screen.").show();
        }
    }

    @FXML private void openFacebook() { openLink("https://www.facebook.com"); }
    @FXML private void openInstagram() { openLink("https://www.instagram.com"); }
    @FXML private void openX() { openLink("https://x.com"); }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Cannot open browser.").show();
        }
    }
}
