package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class RevenueController extends BaseController {

    @FXML private BarChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> occupancyChart;
    @FXML private BarChart<String, Number> peakHoursChart;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label peakHourLabel;
    @FXML private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSummary();
        loadDailyRevenue();
        loadOccupancyData();
        loadPeakHours();
    }

    private void loadSummary() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/reports/summary")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String b = task.getValue();
            try {
                String revenue = b.split("\"totalRevenue\":")[1].split("[,}]")[0].trim();
                String bookings = b.split("\"totalBookings\":")[1].split("[,}]")[0].trim();
                totalRevenueLabel.setText("₹" + revenue);
                totalBookingsLabel.setText(bookings);
            } catch (Exception ex) {
                totalRevenueLabel.setText("N/A");
                totalBookingsLabel.setText("N/A");
            }
        }));
        task.setOnFailed(e -> Platform.runLater(() -> totalRevenueLabel.setText("Cannot connect")));
        new Thread(task).start();
    }

    private void loadDailyRevenue() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/reports/revenue?period=daily")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            revenueChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Revenue (₹)");
            try {
                for (String part : task.getValue().split("\\{")) {
                    if (!part.contains("date")) continue;
                    String date = part.split("\"date\":\"")[1].split("\"")[0];
                    double rev = Double.parseDouble(part.split("\"revenue\":")[1].split("[,}]")[0].trim());
                    series.getData().add(new XYChart.Data<>(date.length() >= 10 ? date.substring(5) : date, rev));
                }
            } catch (Exception ex) { System.out.println("Error parsing revenue: " + ex.getMessage()); }
            if (series.getData().isEmpty()) series.getData().add(new XYChart.Data<>("No data", 0));
            revenueChart.getData().add(series);
            revenueChart.setTitle("Daily Revenue");
        }));
        task.setOnFailed(e -> System.out.println("Failed to load daily revenue"));
        new Thread(task).start();
    }

    private void loadOccupancyData() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/admin/stats")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            occupancyChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Occupancy %");
            try {
                double rate = Double.parseDouble(task.getValue().split("\"occupancyRate\":\"")[1].split("%")[0].trim());
                series.getData().add(new XYChart.Data<>("Current", rate));
            } catch (Exception ex) { series.getData().add(new XYChart.Data<>("Current", 0)); }
            occupancyChart.getData().add(series);
            occupancyChart.setTitle("Current Occupancy %");
        }));
        task.setOnFailed(e -> System.out.println("Failed to load occupancy"));
        new Thread(task).start();
    }

    private void loadPeakHours() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override protected String call() throws Exception {
                HttpRequest r = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/reports/peak-hours")).GET().build();
                return httpClient.send(r, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            peakHoursChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Bookings per Hour");

            String peakHour = "";
            int peakCount = 0;

            try {
                for (String part : task.getValue().split("\\{")) {
                    if (!part.contains("hour")) continue;
                    String hour = part.split("\"hour\":\"")[1].split("\"")[0];
                    int bookings = Integer.parseInt(part.split("\"bookings\":")[1].split("[,}]")[0].trim());
                    series.getData().add(new XYChart.Data<>(hour, bookings));

                    if (bookings > peakCount) {
                        peakCount = bookings;
                        peakHour = hour;
                    }
                }
            } catch (Exception ex) { System.out.println("Error parsing peak hours: " + ex.getMessage()); }

            if (series.getData().isEmpty()) {
                series.getData().add(new XYChart.Data<>("No data", 0));
                peakHourLabel.setText("No check-in data yet");
            } else {
                peakHourLabel.setText("🔥 Busiest hour: " + peakHour + " (" + peakCount + " bookings)");
            }

            peakHoursChart.getData().add(series);
            peakHoursChart.setTitle("Peak Hours");
        }));
        task.setOnFailed(e -> System.out.println("Failed to load peak hours"));
        new Thread(task).start();
    }

    @FXML private void goBack() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, backBtn); }
}