package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
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
        loadSlots();
        loadStats();
    }

    private void setupTable() {
        slotNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        slotStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadSlots() {
        slotTable.getItems().clear();
        // Mock data
        String[] names = {"A1","A2","A3","A4","A5","A6","A7","A8"};
        boolean[] occupied = {false, false, true, false, true, false, false, true};

        for (int i = 0; i < names.length; i++) {
            slotTable.getItems().add(
                new SlotRow(names[i], occupied[i] ? "Occupied" : "Available")
            );
        }
    }

    private void loadStats() {
        totalSlotsLabel.setText("Total Slots: 8");
        occupiedSlotsLabel.setText("Occupied: 3");
        availableSlotsLabel.setText("Available: 5");
    }

    @FXML
    private void handleRefresh() {
        loadSlots();
        loadStats();
    }

    @FXML
    private void goToAnalytics() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/revenue.fxml")
            );
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/dashboard.fxml")
            );
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Inner class for TableView rows
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