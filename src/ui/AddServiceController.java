package ui;

import dao.ServiceDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Service;
import util.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddServiceController {

    @FXML private StackPane stackRoot;
    @FXML private ImageView bgImage;
    @FXML private Region overlay;

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryCombo;

    @FXML private TextField pickupField;
    @FXML private TextField dropoffField;

    @FXML private TextField weightField;
    @FXML private TextField priceField;

    @FXML private DatePicker deadlinePicker;
    @FXML private TextArea descriptionArea;

    @FXML private Label statusLabel;

    private final ServiceDao serviceDao = new ServiceDao();

    @FXML
    private void initialize() {

        // Make background fit window
        if (stackRoot != null && bgImage != null) {
            bgImage.fitWidthProperty().bind(stackRoot.widthProperty());
            bgImage.fitHeightProperty().bind(stackRoot.heightProperty());
        }
        if (stackRoot != null && overlay != null) {
            overlay.prefWidthProperty().bind(stackRoot.widthProperty());
            overlay.prefHeightProperty().bind(stackRoot.heightProperty());
        }

        // categories
        if (categoryCombo != null) {
            categoryCombo.getItems().setAll("Documents", "Electronics", "Clothes", "Others");
            categoryCombo.getSelectionModel().selectFirst();
        }

        // optional: default deadline = tomorrow
        if (deadlinePicker != null) {
            deadlinePicker.setValue(LocalDate.now().plusDays(1));
        }
    }

    @FXML
    private void onCancel() {
        closeThisWindow();
    }

    @FXML
    private void onCreate() {
        statusLabel.setText("");

        // Basic validation
        String title = safe(titleField.getText()).trim();
        String cat = categoryCombo == null ? "" : safe(categoryCombo.getValue()).trim();
        String pickup = safe(pickupField.getText()).trim();
        String dropoff = safe(dropoffField.getText()).trim();
        String desc = safe(descriptionArea.getText()).trim();

        Double weight = parseDouble(weightField);
        Double price = parseDouble(priceField);

        LocalDate deadlineDate = (deadlinePicker == null) ? null : deadlinePicker.getValue();
        LocalDateTime deadline = deadlineDate == null ? null : deadlineDate.atTime(23, 59);

        if (title.isEmpty()) { statusLabel.setText("Title is required."); return; }
        if (pickup.isEmpty()) { statusLabel.setText("Pickup address is required."); return; }
        if (dropoff.isEmpty()) { statusLabel.setText("Dropoff address is required."); return; }
        if (weight == null || weight <= 0) { statusLabel.setText("Weight must be a valid positive number."); return; }
        if (price == null || price <= 0) { statusLabel.setText("Offer price must be a valid positive number."); return; }
        if (deadline == null) { statusLabel.setText("Please choose a delivery deadline."); return; }

        // Create Service object (adjust setters/constructor based on your model)
        Service s = new Service();

        // NOTE: rename these setters if your Service model uses different names
        s.setTitle(title);
        s.setCategory(cat);
        s.setPickupAddress(pickup);
        s.setDropoffAddress(dropoff);
        s.setDescription(desc);
        s.setParcelWeightKg(weight);
        s.setOfferPrice(price);
        s.setDeliveryDeadline(deadline);

        // Optional: store who created it (if you have created_by in DB)
        // s.setUserId(Session.getCurrentUserId());

        s.setCreatedAt(LocalDateTime.now()); // if your DB handles this, you can remove

        try {
            // TODO: call your DAO insert method (depends on your ServiceDao)
            // serviceDao.insert(s);

            System.out.println("✅ Created service: " + s.getTitle());
            closeThisWindow();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to create service: " + e.getMessage());
        }
    }

    private void closeThisWindow() {
        Stage stage = (Stage) (stackRoot != null ? stackRoot.getScene().getWindow() : null);
        if (stage != null) stage.close();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static Double parseDouble(TextField tf) {
        if (tf == null) return null;
        String v = safe(tf.getText()).trim();
        if (v.isEmpty()) return null;
        try { return Double.parseDouble(v); }
        catch (Exception ignored) { return null; }
    }
}
