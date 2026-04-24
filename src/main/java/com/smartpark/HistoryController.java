package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class HistoryController extends BaseController {

    @FXML private TableView<BookingRow> historyTable;
    @FXML private TableColumn<BookingRow, String> colId;
    @FXML private TableColumn<BookingRow, String> colSlot;
    @FXML private TableColumn<BookingRow, String> colPlate;
    @FXML private TableColumn<BookingRow, String> colStatus;
    @FXML private TableColumn<BookingRow, String> colAmount;
    @FXML private TableColumn<BookingRow, String> colCheckIn;
    @FXML private TableColumn<BookingRow, String> colCheckOut;
    @FXML private TextField searchField;
    @FXML private Label totalLabel;
    @FXML private Label revenueLabel;
    @FXML private Button backBtn;

    private java.util.List<BookingRow> allRows = new java.util.ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadHistory();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSlot.setCellValueFactory(new PropertyValueFactory<>("slotId"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("vehiclePlate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));

        // Color rows by status
        historyTable.setRowFactory(tv -> new TableRow<BookingRow>() {
            @Override
            protected void updateItem(BookingRow item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getStatus()) {
                        case "COMPLETED" -> setStyle("-fx-background-color:#e8f5e9;");
                        case "ACTIVE"    -> setStyle("-fx-background-color:#e3f2fd;");
                        case "CANCELLED" -> setStyle("-fx-background-color:#ffebee;");
                        default          -> setStyle("-fx-background-color:#fff8e1;");
                    }
                }
            }
        });
    }

    private void loadHistory() {
        int userId = Session.getInstance().getUserId();

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/bookings/user/" + userId))
                        .GET().build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            allRows.clear();
            historyTable.getItems().clear();

            try {
                String[] parts = body.split("\\{");
                double totalRevenue = 0;
                int count = 0;

                for (String part : parts) {
                    if (!part.contains("vehiclePlate")) continue;

                    String id = part.split("\"id\":")[1].split("[,}]")[0].trim();
                    String slotId = part.split("\"slotId\":")[1].split("[,}]")[0].trim();
                    String plate = part.split("\"vehiclePlate\":\"")[1].split("\"")[0];
                    String status = part.split("\"status\":\"")[1].split("\"")[0];

                    String amount = "0.0";
                    try { amount = part.split("\"totalAmount\":")[1].split("[,}]")[0].trim(); }
                    catch (Exception ex) {}

                    String checkIn = "—";
                    try { checkIn = part.split("\"checkInTime\":\"")[1].split("\"")[0].replace("T", " "); }
                    catch (Exception ex) {}

                    String checkOut = "—";
                    try { checkOut = part.split("\"checkOutTime\":\"")[1].split("\"")[0].replace("T", " "); }
                    catch (Exception ex) {}

                    totalRevenue += Double.parseDouble(amount);
                    count++;

                    allRows.add(new BookingRow(id, "Slot " + slotId, plate, status, "₹" + amount, checkIn, checkOut));
                }

                historyTable.getItems().addAll(allRows);
                totalLabel.setText("Total Bookings: " + count);
                revenueLabel.setText("Total Spent: ₹" + totalRevenue);

            } catch (Exception ex) {
                System.out.println("Error parsing history: " + ex.getMessage());
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() ->
            totalLabel.setText("Failed to load history")
        ));

        new Thread(task).start();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        historyTable.getItems().clear();

        if (query.isEmpty()) {
            historyTable.getItems().addAll(allRows);
        } else {
            allRows.stream()
                .filter(r -> r.getVehiclePlate().toLowerCase().contains(query)
                          || r.getStatus().toLowerCase().contains(query)
                          || r.getId().contains(query))
                .forEach(r -> historyTable.getItems().add(r));
        }

        totalLabel.setText("Showing: " + historyTable.getItems().size() + " bookings");
    }

    @FXML
    private void handleClear() {
        searchField.clear();
        historyTable.getItems().clear();
        historyTable.getItems().addAll(allRows);
        totalLabel.setText("Total Bookings: " + allRows.size());
    }

    @FXML
    private void goToDashboard() {
        loadPage("/com/smartpark/dashboard.fxml", 900, 600, backBtn);
    }

    // BookingRow model
    public static class BookingRow {
        private final String id, slotId, vehiclePlate, status, amount, checkIn, checkOut;

        public BookingRow(String id, String slotId, String vehiclePlate,
                          String status, String amount, String checkIn, String checkOut) {
            this.id = id; this.slotId = slotId; this.vehiclePlate = vehiclePlate;
            this.status = status; this.amount = amount;
            this.checkIn = checkIn; this.checkOut = checkOut;
        }

        public String getId()           { return id; }
        public String getSlotId()       { return slotId; }
        public String getVehiclePlate() { return vehiclePlate; }
        public String getStatus()       { return status; }
        public String getAmount()       { return amount; }
        public String getCheckIn()      { return checkIn; }
        public String getCheckOut()     { return checkOut; }
    }
}