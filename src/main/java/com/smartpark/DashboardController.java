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

    @FXML private Label userLabel;
    @FXML private FlowPane slotGrid;
    @FXML private Label lastUpdatedLabel;

    private Timeline autoRefresh;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLabel.setText("Welcome, " + Session.getInstance().getUsername() + "!");
        loadSlots();
        startAutoRefresh();
    }

    // ── Day 15: Auto-refresh every 5 seconds ──
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
                    request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> renderSlots());
        task.setOnFailed(e -> renderSlots());

        new Thread(task).start();
    }

    private void renderSlots() {
        slotGrid.getChildren().clear();
        String[] slotNames = {"A1","A2","A3","A4","A5","A6","A7","A8"};
        boolean[] occupied = {false, false, true, false, true, false, false, true};

        for (int i = 0; i < slotNames.length; i++) {
            Button slotBtn = new Button(slotNames[i]);
            slotBtn.setPrefSize(90, 90);
            boolean isOccupied = occupied[i];
            slotBtn.setStyle(
                "-fx-background-color:" + (isOccupied ? "#f44336" : "#4caf50") +
                "; -fx-text-fill:white; -fx-font-weight:bold;" +
                "-fx-font-size:14px; -fx-background-radius:8;"
            );

            if (!isOccupied) {
                final String slotName = slotNames[i];
                slotBtn.setOnAction(e -> goToBooking(slotName));
            } else {
                slotBtn.setDisable(true);
            }

            slotGrid.getChildren().add(slotBtn);
        }

        // Update last refreshed time
        if (lastUpdatedLabel != null) {
            java.time.LocalTime now = java.time.LocalTime.now();
            lastUpdatedLabel.setText("Last updated: " +
                String.format("%02d:%02d:%02d",
                    now.getHour(), now.getMinute(), now.getSecond())
            );
        }
    }

    private void goToBooking(String slotName) {
        stopAutoRefresh();
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/booking.fxml")
            );
            Stage stage = (Stage) slotGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void refresh() { loadSlots(); }

    @FXML
    private void handleRefresh() { loadSlots(); }

    @FXML
    private void handleLogout() {
        stopAutoRefresh();
        Session.getInstance().setToken(null);
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/login.fxml")
            );
            Stage stage = (Stage) slotGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}