package ui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Session;
import model.User;

import dao.ServiceDao;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;

public class HomeController {

    // ===== root / background =====
    @FXML private StackPane stackRoot;
    @FXML private ImageView bgImage;
    @FXML private Region overlay;
    @FXML private BorderPane root;
    @FXML private StackPane floatingPane;

    // ===== top controls =====
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;

    // ===== menus =====
    @FXML private MenuButton filtersBtn;
    @FXML private MenuButton accountBtn;

    // ===== account items =====
    @FXML private MenuItem accountInfoItem;
    @FXML private MenuItem editAccountItem;
    @FXML private MenuItem signOutItem;

    // ===== filter fields =====
    @FXML private TextField priceMinField;
    @FXML private TextField priceMaxField;
    @FXML private TextField originField;
    @FXML private TextField destinationField;

    @FXML private DatePicker postedFromPicker;
    @FXML private DatePicker postedToPicker;

    @FXML private DatePicker expectedFromPicker;
    @FXML private DatePicker expectedToPicker;

    @FXML private Button applyFiltersBtn;
    @FXML private Button clearFiltersBtn;

    // ===== floating buttons =====
    @FXML private Button addServiceBtn;
    @FXML private Button messagesBtn;

    // ===== categories =====
    @FXML private ToggleButton catAll;
    @FXML private ToggleButton catDocuments;
    @FXML private ToggleButton catElectronics;
    @FXML private ToggleButton catClothes;
    @FXML private ToggleButton catOthers;

    // ===== table =====
    @FXML private TableView<Service> servicesTable;
    @FXML private TableColumn<Service, String> colTitle;
    @FXML private TableColumn<Service, String> colRoute;
    @FXML private TableColumn<Service, String> colCategory;
    @FXML private TableColumn<Service, Number> colWeight;
    @FXML private TableColumn<Service, Number> colPrice;
    @FXML private TableColumn<Service, LocalDateTime> colArrival;
    @FXML private TableColumn<Service, LocalDateTime> colPosted;

    private final ServiceDao serviceDao = new ServiceDao();

    private final ObservableList<Service> master = FXCollections.observableArrayList();
    private final FilteredList<Service> filtered = new FilteredList<>(master, s -> true);
    private final SortedList<Service> sorted = new SortedList<>(filtered);

    private final ToggleGroup categoryGroup = new ToggleGroup();

    @FXML
    private void initialize() {

        // ===== Make bg/overlay fit screen =====
        if (stackRoot != null && bgImage != null) {
            bgImage.fitWidthProperty().bind(stackRoot.widthProperty());
            bgImage.fitHeightProperty().bind(stackRoot.heightProperty());
        }
        if (stackRoot != null && overlay != null) {
            overlay.prefWidthProperty().bind(stackRoot.widthProperty());
            overlay.prefHeightProperty().bind(stackRoot.heightProperty());
        }

        // ✅ IMPORTANT: DO NOT force toFront()/toBack() here.
        // Layering is handled by the order in FXML and pickOnBounds.

        // ===== rows spacing =====
        if (servicesTable != null) {
            servicesTable.setRowFactory(tv -> {
                TableRow<Service> row = new TableRow<>();
                row.setPrefHeight(70);
                return row;
            });
        }

        // ===== columns =====
        if (colTitle != null) {
            colTitle.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getTitle())));
        }
        if (colRoute != null) {
            colRoute.setCellValueFactory(cd -> {
                Service s = cd.getValue();
                String route = safe(s.getPickupAddress()) + "  →  " + safe(s.getDropoffAddress());
                return new SimpleStringProperty(route);
            });
        }
        if (colCategory != null) {
            colCategory.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getCategory())));
        }
        if (colWeight != null) {
            colWeight.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().getParcelWeightKg()));
        }
        if (colPrice != null) {
            colPrice.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().getOfferPrice()));
        }
        if (colArrival != null) {
            colArrival.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getDeliveryDeadline()));
        }
        if (colPosted != null) {
            colPosted.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getCreatedAt()));
        }

        // ===== bind list =====
        if (servicesTable != null) {
            servicesTable.setItems(sorted);
            // shows horizontal scroll when needed
            servicesTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        }

        // ===== categories group =====
        if (catAll != null) catAll.setToggleGroup(categoryGroup);
        if (catDocuments != null) catDocuments.setToggleGroup(categoryGroup);
        if (catElectronics != null) catElectronics.setToggleGroup(categoryGroup);
        if (catClothes != null) catClothes.setToggleGroup(categoryGroup);
        if (catOthers != null) catOthers.setToggleGroup(categoryGroup);
        if (catAll != null) catAll.setSelected(true);
        if (servicesTable != null) {
            servicesTable.setRowFactory(tv -> {
                TableRow<Service> row = new TableRow<>();
                row.setPrefHeight(70);

                row.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !row.isEmpty()) { // double click
                        openServiceDetails(row.getItem());
                    }
                });

                return row;
            });
        }

        categoryGroup.selectedToggleProperty().addListener((obs, o, n) -> applyFilters());

        // ===== search =====
        if (searchField != null) {
            searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        }

        // ===== filter listeners =====
        addTextListener(priceMinField);
        addTextListener(priceMaxField);
        addTextListener(originField);
        addTextListener(destinationField);

        if (postedFromPicker != null) postedFromPicker.valueProperty().addListener((obs, o, n) -> applyFilters());
        if (postedToPicker != null)   postedToPicker.valueProperty().addListener((obs, o, n) -> applyFilters());
        if (expectedFromPicker != null) expectedFromPicker.valueProperty().addListener((obs, o, n) -> applyFilters());
        if (expectedToPicker != null)   expectedToPicker.valueProperty().addListener((obs, o, n) -> applyFilters());

        if (applyFiltersBtn != null) applyFiltersBtn.setOnAction(e -> applyFilters());
        if (clearFiltersBtn != null) clearFiltersBtn.setOnAction(e -> clearFilters());

        // ===== sort =====
        if (sortCombo != null) {
            sortCombo.getItems().setAll(
                    "Most Recent",
                    "Oldest",
                    "Cheap (Low → High)",
                    "Expensive (High → Low)",
                    "Expected Soonest",
                    "Expected Latest"
            );
            sortCombo.setValue("Most Recent");
            sortCombo.valueProperty().addListener((obs, o, n) -> applySort(n));
        }
        applySort(sortCombo == null ? "Most Recent" : sortCombo.getValue());

        // ===== account menu actions =====
        if (accountInfoItem != null) accountInfoItem.setOnAction(e -> openAccountInfoWindow());
        if (editAccountItem != null) editAccountItem.setOnAction(e -> openEditAccountWindow());
        if (signOutItem != null) signOutItem.setOnAction(e -> signOut());

        // ===== load =====
        reload();
    }

    @FXML
    private void onAddService() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/ui/AddService.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Service");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Could not open Add Service");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }


    @FXML
    private void onMessages() {
        System.out.println("💬 Messages clicked");
    }

    private void reload() {
        master.setAll(serviceDao.findAllApproved());
        applyFilters();
    }
    private void openServiceDetails(Service s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/ui/ServiceDetails.fxml"));
            Parent root = loader.load();

            ServiceDetailsController controller = loader.getController();
            controller.setService(s); // pass selected service

            Stage stage = new Stage();
            stage.setTitle("Service Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Could not open service details");
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }


    private void clearFilters() {
        if (priceMinField != null) priceMinField.clear();
        if (priceMaxField != null) priceMaxField.clear();
        if (originField != null) originField.clear();
        if (destinationField != null) destinationField.clear();

        if (postedFromPicker != null) postedFromPicker.setValue(null);
        if (postedToPicker != null)   postedToPicker.setValue(null);

        if (expectedFromPicker != null) expectedFromPicker.setValue(null);
        if (expectedToPicker != null)   expectedToPicker.setValue(null);

        applyFilters();
    }

    private void applyFilters() {
        final String q = (searchField == null) ? "" : safe(searchField.getText()).toLowerCase(Locale.ROOT);
        final String selectedCategory = getSelectedCategory();

        final Double minPrice = parseDoubleOrNull(priceMinField);
        final Double maxPrice = parseDoubleOrNull(priceMaxField);

        final String origin = originField == null ? "" : safe(originField.getText()).toLowerCase(Locale.ROOT);
        final String dest   = destinationField == null ? "" : safe(destinationField.getText()).toLowerCase(Locale.ROOT);

        final LocalDate postedFrom = postedFromPicker == null ? null : postedFromPicker.getValue();
        final LocalDate postedTo   = postedToPicker == null ? null : postedToPicker.getValue();

        final LocalDate expectedFrom = expectedFromPicker == null ? null : expectedFromPicker.getValue();
        final LocalDate expectedTo   = expectedToPicker == null ? null : expectedToPicker.getValue();

        filtered.setPredicate(s -> {
            if (s == null) return false;

            if (selectedCategory != null && !selectedCategory.equalsIgnoreCase(safe(s.getCategory()))) return false;

            if (!q.isEmpty()) {
                String hay = (safe(s.getTitle()) + " " +
                        safe(s.getDescription()) + " " +
                        safe(s.getPickupAddress()) + " " +
                        safe(s.getDropoffAddress()))
                        .toLowerCase(Locale.ROOT);
                if (!hay.contains(q)) return false;
            }

            double price = s.getOfferPrice();
            if (minPrice != null && price < minPrice) return false;
            if (maxPrice != null && price > maxPrice) return false;

            if (!origin.isEmpty() && !safe(s.getPickupAddress()).toLowerCase(Locale.ROOT).contains(origin)) return false;
            if (!dest.isEmpty()   && !safe(s.getDropoffAddress()).toLowerCase(Locale.ROOT).contains(dest)) return false;

            LocalDateTime created = s.getCreatedAt();
            if (postedFrom != null && (created == null || created.toLocalDate().isBefore(postedFrom))) return false;
            if (postedTo != null   && (created == null || created.toLocalDate().isAfter(postedTo))) return false;

            LocalDateTime deadline = s.getDeliveryDeadline();
            if (expectedFrom != null && (deadline == null || deadline.toLocalDate().isBefore(expectedFrom))) return false;
            if (expectedTo != null   && (deadline == null || deadline.toLocalDate().isAfter(expectedTo))) return false;

            return true;
        });
    }

    private String getSelectedCategory() {
        Toggle t = categoryGroup.getSelectedToggle();
        if (t == null || t == catAll) return null;

        if (t == catDocuments) return "Documents";
        if (t == catElectronics) return "Electronics";
        if (t == catClothes) return "Clothes";
        if (t == catOthers) return "Others";
        return null;
    }

    private void applySort(String choice) {
        if (choice == null) choice = "Most Recent";

        Comparator<Service> cmp;

        switch (choice) {
            case "Oldest":
                cmp = Comparator.comparing(Service::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "Cheap (Low → High)":
                cmp = Comparator.comparingDouble(Service::getOfferPrice);
                break;
            case "Expensive (High → Low)":
                cmp = Comparator.comparingDouble(Service::getOfferPrice).reversed();
                break;
            case "Expected Soonest":
                cmp = Comparator.comparing(Service::getDeliveryDeadline, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "Expected Latest":
                cmp = Comparator.comparing(Service::getDeliveryDeadline, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
                break;
            case "Most Recent":
            default:
                cmp = Comparator.comparing(Service::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
                break;
        }

        sorted.setComparator(cmp);
    }



    private void addTextListener(TextField tf) {
        if (tf != null) tf.textProperty().addListener((obs, o, n) -> applyFilters());
    }

    private static String safe(String s) { return s == null ? "" : s; }

    private static Double parseDoubleOrNull(TextField tf) {
        if (tf == null) return null;
        String v = safe(tf.getText()).trim();
        if (v.isEmpty()) return null;
        try { return Double.parseDouble(v); }
        catch (Exception ignored) { return null; }
    }
    private void openAccountInfoWindow() {
        User u = Session.getCurrentUser();
        if (u == null) {
            alert("Not logged in", "Please login first.");
            return;
        }
        openWindow("/resources/ui/accountInfo.fxml", "Account Info");
    }

    private void openEditAccountWindow() {
        User u = Session.getCurrentUser();
        if (u == null) {
            alert("Not logged in", "Please login first.");
            return;
        }
        openWindow("/resources/ui/editAccount.fxml", "Edit Account");
    }
    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            alert("Error", "Failed to open: " + fxmlPath + "\n" + e.getMessage());
        }
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }


    private void signOut() {
        Session.logout();
        System.out.println("🚪 Signed out");

        // TODO: go back to login scene (depends on your app navigation)
        // Example:
        // SceneLoader.setRoot("/ui/Login.fxml");
    }

}
