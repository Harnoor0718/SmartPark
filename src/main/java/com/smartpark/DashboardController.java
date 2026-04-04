package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Refreshable {

    @FXML private Label userLabel;
    @FXML private FlowPane slotGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLabel.setText("Welcome, " + Session.getInstance().getUsername() + "!");
        loadSlots();
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

        task.setOnSucceeded(e -> {
            // Parse and display slots
            slotGrid.getChildren().clear();
            String body = task.getValue();
            // Simple mock display until JSON parsing added
            for (int i = 1; i <= 8; i++) {
                Button slotBtn = new Button("A" + i);
                slotBtn.setPrefSize(80, 80);
                slotBtn.setStyle(
                    "-fx-background-color: #4caf50; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8;"
                );
                slotGrid.getChildren().add(slotBtn);
            }
        });

        task.setOnFailed(e -> {
            // Show mock data if API not reachable
            for (int i = 1; i <= 8; i++) {
                Button slotBtn = new Button("A" + i);
                slotBtn.setPrefSize(80, 80);
                boolean occupied = (i % 3 == 0);
                slotBtn.setStyle(
                    "-fx-background-color: " + (occupied ? "#f44336" : "#4caf50") + 
                    "; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-font-size: 14px; -fx-background-radius: 8;"
                );
                slotGrid.getChildren().add(slotBtn);
            }
        });

        new Thread(task).start();
    }

    @Override
    public void refresh() {
        loadSlots();
    }

    @FXML
    private void handleLogout() {
        Session.getInstance().setToken(null);
        System.out.println("Logged out");
    }
}