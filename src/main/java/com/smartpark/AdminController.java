package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
        slotNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        slotStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadStats();
        loadSlots();
    }

    private void loadStats() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/admin/stats")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String b = task.getValue();
            try {
                totalSlotsLabel.setText("Total Slots: " + b.split("\"totalSlots\":")[1].split("[,}]")[0].trim());
                occupiedSlotsLabel.setText("Occupied: " + b.split("\"occupiedSlots\":")[1].split("[,}]")[0].trim());
                availableSlotsLabel.setText("Available: " + b.split("\"availableSlots\":")[1].split("[,}]")[0].trim());
            } catch (Exception ex) {
                totalSlotsLabel.setText("Total Slots: N/A");
                occupiedSlotsLabel.setText("Occupied: N/A");
                availableSlotsLabel.setText("Available: N/A");
            }
        }));
        task.setOnFailed(e -> Platform.runLater(() -> totalSlotsLabel.setText("Cannot connect to server")));
        new Thread(task).start();
    }

    private void loadSlots() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/admin/slots")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            slotTable.getItems().clear();
            try {
                for (String part : task.getValue().split("\\{")) {
                    if (!part.contains("slotNumber")) continue;
                    String name = part.split("\"slotNumber\":\"")[1].split("\"")[0];
                    slotTable.getItems().add(new SlotRow(name, part.contains("\"isOccupied\":true") ? "Occupied" : "Available"));
                }
            } catch (Exception ex) { System.out.println("Error parsing slots: " + ex.getMessage()); }
        }));
        task.setOnFailed(e -> System.out.println("Failed to load slots"));
        new Thread(task).start();
    }

    @FXML private void handleRefresh()  { loadStats(); loadSlots(); }
    @FXML private void goToAnalytics() { loadPage("/com/smartpark/revenue.fxml",   900, 600, backBtn); }
    @FXML private void goBack()         { loadPage("/com/smartpark/dashboard.fxml", 900, 600, backBtn); }

    public static class SlotRow {
        private final String name, status;
        public SlotRow(String name, String status) { this.name = name; this.status = status; }
        public String getName()   { return name; }
        public String getStatus() { return status; }
    }
}