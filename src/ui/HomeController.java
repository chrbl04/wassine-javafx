package ui;

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
import model.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;

public class HomeController {

    // ===== top controls =====
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;

    // filters (only if in FXML)
    @FXML private TextField priceMinField;
    @FXML private TextField priceMaxField;
    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private DatePicker arrivalFromPicker;
    @FXML private DatePicker arrivalToPicker;

    // categories
    @FXML private ToggleButton catDocuments;
    @FXML private ToggleButton catElectronics;
    @FXML private ToggleButton catClothes;
    @FXML private ToggleButton catOthers;

    // table
    @FXML private TableView<Service> servicesTable;
    @FXML private TableColumn<Service, String> titleCol;
    @FXML private TableColumn<Service, String> categoryCol;
    @FXML private TableColumn<Service, String> pickupCol;
    @FXML private TableColumn<Service, String> dropoffCol;
    @FXML private TableColumn<Service, Number> priceCol;
    @FXML private TableColumn<Service, LocalDateTime> postedCol;
    @FXML private TableColumn<Service, LocalDateTime> arrivalCol;

    private final ServiceDao serviceDao = new ServiceDao();

    // IMPORTANT: typed lists (never raw Object)
    private final ObservableList<Service> master = FXCollections.observableArrayList();
    private final FilteredList<Service> filtered = new FilteredList<>(master, s -> true);
    private final SortedList<Service> sorted = new SortedList<>(filtered);

    private final ToggleGroup categoryGroup = new ToggleGroup();

    @FXML
    private void initialize() {

        // ---- table columns ----
        if (titleCol != null) {
            titleCol.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getTitle())));
        }
        if (categoryCol != null) {
            categoryCol.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getCategory())));
        }
        if (pickupCol != null) {
            pickupCol.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getPickupAddress())));
        }
        if (dropoffCol != null) {
            dropoffCol.setCellValueFactory(cd -> new SimpleStringProperty(safe(cd.getValue().getDropoffAddress())));
        }
        if (priceCol != null) {
            priceCol.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().getOfferPrice()));
        }
        if (postedCol != null) {
            postedCol.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getCreatedAt()));
        }
        if (arrivalCol != null) {
            arrivalCol.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getDeliveryDeadline()));
        }

        servicesTable.setItems(sorted);
        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ---- categories toggle group ----
        if (catDocuments != null)   catDocuments.setToggleGroup(categoryGroup);
        if (catElectronics != null) catElectronics.setToggleGroup(categoryGroup);
        if (catClothes != null)     catClothes.setToggleGroup(categoryGroup);
        if (catOthers != null)      catOthers.setToggleGroup(categoryGroup);

        categoryGroup.selectedToggleProperty().addListener((obs, o, n) -> applyFilters());

        // ---- search ----
        if (searchField != null) {
            searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        }

        // ---- sort ----
        if (sortCombo != null) {
            sortCombo.getItems().setAll("Date Posted", "Price", "Arrival Date");
            sortCombo.setValue("Date Posted");
            sortCombo.valueProperty().addListener((obs, o, n) -> applySort(n));
        }
        applySort("Date Posted");

        // ---- filter listeners ----
        addTextListener(priceMinField);
        addTextListener(priceMaxField);
        addTextListener(originField);
        addTextListener(destinationField);

        if (arrivalFromPicker != null) arrivalFromPicker.valueProperty().addListener((obs, o, n) -> applyFilters());
        if (arrivalToPicker != null)   arrivalToPicker.valueProperty().addListener((obs, o, n) -> applyFilters());

        // ---- initial load ----
        reload();
    }

    private void addTextListener(TextField tf) {
        if (tf != null) tf.textProperty().addListener((obs, o, n) -> applyFilters());
    }

    private void reload() {
        master.setAll(serviceDao.findAllApproved());
        applyFilters();
    }

    private void applyFilters() {
        final String q = (searchField == null) ? "" : safe(searchField.getText()).toLowerCase(Locale.ROOT);
        final String selectedCategory = getSelectedCategory();

        final Double minPrice = parseDoubleOrNull(priceMinField);
        final Double maxPrice = parseDoubleOrNull(priceMaxField);

        final String origin = originField == null ? "" : safe(originField.getText()).toLowerCase(Locale.ROOT);
        final String dest   = destinationField == null ? "" : safe(destinationField.getText()).toLowerCase(Locale.ROOT);

        final LocalDate fromDate = arrivalFromPicker == null ? null : arrivalFromPicker.getValue();
        final LocalDate toDate   = arrivalToPicker == null ? null : arrivalToPicker.getValue();

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

            LocalDateTime deadline = s.getDeliveryDeadline();
            if (fromDate != null && (deadline == null || deadline.toLocalDate().isBefore(fromDate))) return false;
            if (toDate != null   && (deadline == null || deadline.toLocalDate().isAfter(toDate))) return false;

            return true;
        });
    }

    private String getSelectedCategory() {
        Toggle t = categoryGroup.getSelectedToggle();
        if (t == null) return null;

        if (t == catDocuments)   return "Documents";
        if (t == catElectronics) return "Electronics";
        if (t == catClothes)     return "Clothes";
        if (t == catOthers)      return "Others";
        return null;
    }

    // ---- Sorting (use ONE style only) ----
    private void applySort(String choice) {
        if (choice == null) choice = "Date Posted";

        Comparator<Service> cmp;

        if ("Price".equals(choice)) {
            cmp = Comparator.comparingDouble(Service::getOfferPrice);
        } else if ("Arrival Date".equals(choice)) {
            cmp = Comparator.comparing(Service::getDeliveryDeadline,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else {
            cmp = Comparator.comparing(Service::getCreatedAt,
                    Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        }

        sorted.setComparator(cmp);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static Double parseDoubleOrNull(TextField tf) {
        if (tf == null) return null;
        String v = safe(tf.getText()).trim();
        if (v.isEmpty()) return null;
        try {
            return Double.parseDouble(v);
        } catch (Exception ignored) {
            return null;
        }
    }
}
