package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class AdminController extends BaseController {

    @FXML private TableView<SlotRow> slotTable;
    @FXML private TableColumn<SlotRow, String> slotNameCol;
    @FXML private TableColumn<SlotRow, String> slotStatusCol;
    @FXML private Label totalSlotsLabel;
    @FXML private Label occupiedSlotsLabel;
    @FXML private Label availableSlotsLabel;
    @FXML private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadStats();
        loadSlots();
    }

    private void setupTable() {
        slotNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        slotStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadStats() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/admin/stats"))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            try {
                String total = body.split("\"totalSlots\":")[1].split("[,}]")[0].trim();
                String occupied = body.split("\"occupiedSlots\":")[1].split("[,}]")[0].trim();
                String available = body.split("\"availableSlots\":")[1].split("[,}]")[0].trim();

                totalSlotsLabel.setText("Total Slots: " + total);
                occupiedSlotsLabel.setText("Occupied: " + occupied);
                availableSlotsLabel.setText("Available: " + available);
            } catch (Exception ex) {
                totalSlotsLabel.setText("Total Slots: N/A");
                occupiedSlotsLabel.setText("Occupied: N/A");
                availableSlotsLabel.setText("Available: N/A");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            totalSlotsLabel.setText("Cannot connect to server");
        }));

        new Thread(task).start();
    }

    private void loadSlots() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/admin/slots"))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            slotTable.getItems().clear();

            try {
                String[] parts = body.split("\\{");
                for (String part : parts) {
                    if (!part.contains("slotNumber")) continue;

                    String name = part.split("\"slotNumber\":\"")[1].split("\"")[0];
                    boolean isOccupied = part.contains("\"isOccupied\":true");
                    String status = isOccupied ? "Occupied" : "Available";

                    slotTable.getItems().add(new SlotRow(name, status));
                }
            } catch (Exception ex) {
                System.out.println("Error parsing slots: " + ex.getMessage());
            }
        }));

        task.setOnFailed(e -> System.out.println("Failed to load slots"));

        new Thread(task).start();
    }

    @FXML
    private void handleRefresh() {
        loadStats();
        loadSlots();
    }

    @FXML
    private void goToAnalytics() {
        loadPage("/com/smartpark/revenue.fxml", 900, 600);
    }

    @FXML
    private void goBack() {
        loadPage("/com/smartpark/dashboard.fxml", 900, 600);
    }

    private void loadPage(String fxmlPath, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SlotRow {
        private final String name;
        private final String status;

        public SlotRow(String name, String status) {
            this.name = name;
            this.status = status;
        }

        public String getName() { return name; }
        public String getStatus() { return status; }
    }
}