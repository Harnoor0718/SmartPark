package com.smartpark;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Refreshable {

    @FXML private Label navUsername;
    @FXML private Label welcomeName;
    @FXML private Label userRole;

    @FXML private Label totalSlots;
    @FXML private Label availableSlots;
    @FXML private Label occupiedSlots;

    @FXML private FlowPane slotGrid;
    @FXML private Label lastUpdated;

    private Timeline autoRefresh;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String username = Session.getInstance().getUsername();
        String role = Session.getInstance().getRole();

        navUsername.setText(username);
        welcomeName.setText(username);
        userRole.setText(role != null ? role.toUpperCase() : "");

        loadSlots();
        startAutoRefresh();
    }

    // ─────────────────────────────
    // Auto Refresh
    // ─────────────────────────────
    private void startAutoRefresh() {
        autoRefresh = new Timeline(
            new KeyFrame(Duration.seconds(5), e -> loadSlots())
        );
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    private void stopAutoRefresh() {
        if (autoRefresh != null) autoRefresh.stop();
    }

    // ─────────────────────────────
    // API Call
    // ─────────────────────────────
    private void loadSlots() {

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/slots"))
                        .header("Authorization", "Bearer " + Session.getInstance().getToken())
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                return response.body();
            }
        };

        task.setOnSucceeded(e -> renderSlots(task.getValue()));
        task.setOnFailed(e -> System.out.println("Failed to load slots"));

        new Thread(task).start();
    }

    // ─────────────────────────────
    // Render UI (same as HTML)
    // ─────────────────────────────
    private void renderSlots(String json) {

        slotGrid.getChildren().clear();

        // ⚠️ SIMPLE parsing (you can improve later)
        String[] slots = json.split("\\{");

        int total = 0, available = 0, occupied = 0;

        for (String s : slots) {

            if (!s.contains("slotNumber")) continue;

            total++;

            String number = s.split("\"slotNumber\":\"")[1].split("\"")[0];
            String type = s.split("\"slotType\":\"")[1].split("\"")[0];
            boolean isOccupied = s.contains("\"occupied\":true");

            if (isOccupied) occupied++;
            else available++;

            Button btn = new Button();
            btn.setPrefSize(100, 80);

            btn.setText(number + "\n" + type);

            btn.setStyle(
                "-fx-background-color:" + (isOccupied ? "#e74c3c" : "#4caf50") + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
            );

            if (!isOccupied) {
                btn.setOnAction(e -> goToBooking(number, type));
            } else {
                btn.setDisable(true);
            }

            slotGrid.getChildren().add(btn);
        }

        // Stats update
        totalSlots.setText(String.valueOf(total));
        availableSlots.setText(String.valueOf(available));
        occupiedSlots.setText(String.valueOf(occupied));

        // Last updated time
        java.time.LocalTime now = java.time.LocalTime.now();
        lastUpdated.setText(
            String.format("%02d:%02d:%02d",
                now.getHour(), now.getMinute(), now.getSecond())
        );
    }

    // ─────────────────────────────
    // Navigation
    // ─────────────────────────────
    private void goToBooking(String number, String type) {
        stopAutoRefresh();

        try {
            Session.getInstance().setSelectedSlot(number, type);

            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/booking.fxml")
            );

            Stage stage = (Stage) slotGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        stopAutoRefresh();

        Session.getInstance().clear();

        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/login.fxml")
            );

            Stage stage = (Stage) slotGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdmin() {
        stopAutoRefresh();

        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/admin.fxml")
            );

            Stage stage = (Stage) slotGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() {
        loadSlots();
    }
}