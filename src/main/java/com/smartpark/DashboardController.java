package com.smartpark;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
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

        navUsername.setText(username != null ? username : "");
        welcomeName.setText(username != null ? username : "");
        userRole.setText(role != null ? role.toUpperCase() : "");

        loadSlots();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        autoRefresh = new Timeline(new KeyFrame(Duration.seconds(5), e -> loadSlots()));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    private void stopAutoRefresh() {
        if (autoRefresh != null) autoRefresh.stop();
    }

    private void loadSlots() {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/slots")).GET().build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> renderSlots(task.getValue())));
        task.setOnFailed(e -> System.out.println("Failed to load slots: " + task.getException()));
        new Thread(task).start();
    }

    private void renderSlots(String json) {
        slotGrid.getChildren().clear();
        int total = 0, available = 0, occupied = 0;

        try {
            String[] parts = json.split("\\{");
            for (String part : parts) {
                if (!part.contains("slotNumber")) continue;
                total++;
                String number = part.split("\"slotNumber\":\"")[1].split("\"")[0];
                String type = part.split("\"slotType\":\"")[1].split("\"")[0];
                int slotId = Integer.parseInt(part.split("\"id\":")[1].split("[,}]")[0].trim());
                boolean isOccupied = part.contains("\"occupied\":true");

                if (isOccupied) occupied++; else available++;

                Button btn = new Button(number + "\n" + type);
                btn.setPrefSize(100, 80);
                btn.setStyle(
                    "-fx-background-color:" + (isOccupied ? "#e74c3c" : "#4caf50") + ";" +
                    "-fx-text-fill:white;-fx-font-weight:bold;" +
                    "-fx-background-radius:8;-fx-font-size:13px;"
                );
                if (!isOccupied) btn.setOnAction(e -> goToBooking(number, type, slotId));
                else btn.setDisable(true);
                slotGrid.getChildren().add(btn);
            }
        } catch (Exception e) {
            System.out.println("Error parsing slots: " + e.getMessage());
        }

        totalSlots.setText(String.valueOf(total));
        availableSlots.setText(String.valueOf(available));
        occupiedSlots.setText(String.valueOf(occupied));

        java.time.LocalTime now = java.time.LocalTime.now();
        lastUpdated.setText("Last updated: %02d:%02d:%02d".formatted(now.getHour(), now.getMinute(), now.getSecond()));
    }

    private void goToBooking(String number, String type, int slotId) {
        stopAutoRefresh();
        Session.getInstance().setSelectedSlot(number, type, slotId);
        loadPage("/com/smartpark/booking.fxml", 900, 600, slotGrid);
    }

    @FXML private void goToCheckIn() { stopAutoRefresh(); loadPage("/com/smartpark/checkin.fxml", 900, 600, slotGrid); }
    @FXML private void goToAdmin()   { stopAutoRefresh(); loadPage("/com/smartpark/admin.fxml",   900, 600, slotGrid); }

    @FXML
    private void handleLogout() {
        stopAutoRefresh();
        Session.getInstance().clear();
        loadPage("/com/smartpark/login.fxml", 900, 600, slotGrid);
    }

    @Override
    public void refresh() { loadSlots(); }
}