package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class RevenueController extends BaseController {

    @FXML private BarChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> occupancyChart;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSummary();
        loadDailyRevenue();
        loadOccupancyData();
    }

    private void loadSummary() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/reports/summary"))
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
                String revenue = body.split("\"totalRevenue\":")[1].split("[,}]")[0].trim();
                String bookings = body.split("\"totalBookings\":")[1].split("[,}]")[0].trim();

                totalRevenueLabel.setText("Total Revenue: ₹" + revenue);
                totalBookingsLabel.setText("Total Bookings: " + bookings);
            } catch (Exception ex) {
                totalRevenueLabel.setText("Total Revenue: N/A");
                totalBookingsLabel.setText("Total Bookings: N/A");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            totalRevenueLabel.setText("Cannot connect to server");
        }));

        new Thread(task).start();
    }

    private void loadDailyRevenue() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/reports/revenue?period=daily"))
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
            revenueChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Revenue (₹)");

            try {
                String[] parts = body.split("\\{");
                for (String part : parts) {
                    if (!part.contains("date")) continue;

                    String date = part.split("\"date\":\"")[1].split("\"")[0];
                    String revenueStr = part.split("\"revenue\":")[1].split("[,}]")[0].trim();
                    double revenue = Double.parseDouble(revenueStr);

                    // Shorten date for display (show only MM-DD)
                    String shortDate = date.length() >= 10 ? date.substring(5) : date;
                    series.getData().add(new XYChart.Data<>(shortDate, revenue));
                }
            } catch (Exception ex) {
                System.out.println("Error parsing daily revenue: " + ex.getMessage());
            }

            // If no data from API, show placeholder
            if (series.getData().isEmpty()) {
                series.getData().add(new XYChart.Data<>("No data", 0));
            }

            revenueChart.getData().add(series);
            revenueChart.setTitle("Daily Revenue");
        }));

        task.setOnFailed(e -> System.out.println("Failed to load daily revenue"));

        new Thread(task).start();
    }

    private void loadOccupancyData() {
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
            occupancyChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Occupancy %");

            try {
                String occupancyRate = body.split("\"occupancyRate\":\"")[1].split("%")[0].trim();
                double rate = Double.parseDouble(occupancyRate);
                series.getData().add(new XYChart.Data<>("Current", rate));
            } catch (Exception ex) {
                series.getData().add(new XYChart.Data<>("Current", 0));
            }

            occupancyChart.getData().add(series);
            occupancyChart.setTitle("Current Occupancy %");
        }));

        task.setOnFailed(e -> System.out.println("Failed to load occupancy data"));

        new Thread(task).start();
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
}