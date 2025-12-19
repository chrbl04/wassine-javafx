package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Service;

import java.time.LocalDateTime;

public class ServiceDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private Label weightLabel;

    @FXML private Label pickupLabel;
    @FXML private Label dropoffLabel;

    @FXML private Label postedLabel;
    @FXML private Label expectedLabel;

    @FXML private Label descriptionLabel;

    private Service service;

    public void setService(Service s) {
        this.service = s;
        fillUI();
    }

    private void fillUI() {
        if (service == null) return;

        titleLabel.setText(safe(service.getTitle()));
        categoryLabel.setText(safe(service.getCategory()));

        priceLabel.setText(String.format("%.2f", service.getOfferPrice()));
        weightLabel.setText(String.format("%.2f", service.getParcelWeightKg()));

        pickupLabel.setText(safe(service.getPickupAddress()));
        dropoffLabel.setText(safe(service.getDropoffAddress()));

        LocalDateTime created = service.getCreatedAt();
        LocalDateTime deadline = service.getDeliveryDeadline();

        postedLabel.setText(created == null ? "-" : created.toString());
        expectedLabel.setText(deadline == null ? "-" : deadline.toString());

        descriptionLabel.setText(safe(service.getDescription()));
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
