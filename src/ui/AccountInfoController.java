package ui;

import dao.UserDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.User;
import util.Session;

public class AccountInfoController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label idLabel;

    private final UserDao userDao = new UserDao();

    @FXML
    private void initialize() {
        int id = Session.getCurrentUserId();
        if (id == -1) {
            setEmpty();
            return;
        }

        try {
            User u = userDao.findById(id);   // ✅ fresh from DB
            if (u == null) {
                setEmpty();
                return;
            }

            // optional: keep session up to date
            Session.setCurrentUser(u);

            nameLabel.setText(safe(u.getFirstName()) + " " + safe(u.getLastName()));
            emailLabel.setText(safe(u.getEmail()));

            // If you want country code + phone displayed together:
            String phone = safe(u.getCountryCode()) + " " + safe(u.getPhone());
            phoneLabel.setText(phone.trim().isEmpty() ? "-" : phone.trim());

            idLabel.setText(String.valueOf(u.getUserId()));

        } catch (Exception e) {
            e.printStackTrace();
            setEmpty();
        }
    }

    private void setEmpty() {
        nameLabel.setText("Not available");
        emailLabel.setText("-");
        phoneLabel.setText("-");
        idLabel.setText("-");
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
