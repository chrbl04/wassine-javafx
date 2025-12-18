package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import util.Database; // <-- your real Database.java

public class LoginController {
    @FXML private GridPane root;
    @FXML private StackPane leftPane;
    @FXML private ImageView bgImage;
    @FXML private Rectangle overlay;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void initialize() {
        // ===== background (your existing code) =====
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

        bgImage.toBack();
        overlay.toFront();
    }

    // ===================== LOGIN (DB CHECK) =====================
    @FXML
    private void onSignIn() {
        String email = emailField.getText().trim();
        String pass  = passwordField.getText(); // for now assumes DB stores plain pass in password_hash

        if (email.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter email and password.");
            return;
        }

        boolean ok = checkUser(email, pass);

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "✅ Successful login!");
            // TODO: navigate to Home scene
        } else {
            showAlert(Alert.AlertType.ERROR, "❌ Invalid email or password.");
        }
    }

    private boolean checkUser(String email, String passwordHashValue) {
        String sql = "SELECT user_id FROM users WHERE email = ? AND password_hash = ? LIMIT 1";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHashValue);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error:\n" + e.getMessage());
            return false;
        }
    }

    @FXML
    private void onSignUp() {
        util.SceneLoader.load("/resources/ui/Signup.fxml");
    }


    @FXML
    private void onReset() {
        new Alert(Alert.AlertType.INFORMATION, "Password reset flow coming soon.").show();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }

    // ===================== SOCIAL LINKS =====================
    @FXML private void openFacebook() { openWeb("https://www.facebook.com/"); }
    @FXML private void openInstagram(){ openWeb("https://www.instagram.com/"); }
    @FXML private void openX()        { openWeb("https://x.com/"); }

    private void openWeb(String url) {
        try {
            {
                Desktop.getDesktop().browse(new URI(url));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not open browser:\n" + e.getMessage());
        }
    }
}
