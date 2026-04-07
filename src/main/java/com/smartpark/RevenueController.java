package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class RevenueController extends BaseController {

    @FXML private BarChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> occupancyChart;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRevenueChart();
        loadOccupancyChart();
        loadSummary();
    }

    private void loadRevenueChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Revenue (₹)");
        series.getData().add(new XYChart.Data<>("Mon", 450));
        series.getData().add(new XYChart.Data<>("Tue", 620));
        series.getData().add(new XYChart.Data<>("Wed", 380));
        series.getData().add(new XYChart.Data<>("Thu", 710));
        series.getData().add(new XYChart.Data<>("Fri", 890));
        series.getData().add(new XYChart.Data<>("Sat", 1200));
        series.getData().add(new XYChart.Data<>("Sun", 950));
        revenueChart.getData().add(series);
        revenueChart.setTitle("Weekly Revenue");
    }

    private void loadOccupancyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Occupancy %");
        series.getData().add(new XYChart.Data<>("Mon", 60));
        series.getData().add(new XYChart.Data<>("Tue", 75));
        series.getData().add(new XYChart.Data<>("Wed", 45));
        series.getData().add(new XYChart.Data<>("Thu", 80));
        series.getData().add(new XYChart.Data<>("Fri", 90));
        series.getData().add(new XYChart.Data<>("Sat", 100));
        series.getData().add(new XYChart.Data<>("Sun", 85));
        occupancyChart.getData().add(series);
        occupancyChart.setTitle("Weekly Occupancy");
    }

    private void loadSummary() {
        totalRevenueLabel.setText("Total Revenue: ₹5,200");
        totalBookingsLabel.setText("Total Bookings: 104");
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
}