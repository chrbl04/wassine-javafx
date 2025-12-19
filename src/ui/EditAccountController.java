package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.User;
import util.Session;

public class EditAccountController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML
    private void initialize() {
        User u = Session.getCurrentUser();
        if (u == null) {
            alert("Not logged in", "Please login first.");
            return;
        }

        firstNameField.setText(safe(u.getFirstName()));
        lastNameField.setText(safe(u.getLastName()));
        emailField.setText(safe(u.getEmail()));
        phoneField.setText(safe(u.getPhone()));
    }

    @FXML
    private void onSave() {
        User u = Session.getCurrentUser();
        if (u == null) return;

        u.setFirstName(firstNameField.getText());
        u.setLastName(lastNameField.getText());
        u.setEmail(emailField.getText());
        u.setPhone(phoneField.getText());

        // TODO: update DB using your DAO
        // userDao.update(u);

        Session.setCurrentUser(u); // keep session updated
        alert("Saved", "Account updated successfully.");
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
