package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class CheckinController extends BaseController {

    @FXML private TextField bookingIdField;
    @FXML private Label slotInfoLabel;
    @FXML private Label statusLabel;
    @FXML private Button checkinBtn;
    @FXML private Button checkoutBtn;
    @FXML private Label errorLabel;
    @FXML private Label billLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        billLabel.setVisible(false);
        slotInfoLabel.setText("Enter booking ID to see slot info");
    }

    @FXML
    private void handleCheckin() {
        String bookingId = bookingIdField.getText().trim();

        if (bookingId.isEmpty()) {
            showError(errorLabel, "Please enter booking ID");
            return;
        }

        checkinBtn.setDisable(true);
        checkinBtn.setText("Checking in...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = """
                    {"bookingId":"%s"}
                    """.formatted(bookingId);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/checkin"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Session.getInstance().getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> {
            String body = task.getValue();
            if (body.contains("success")) {
                statusLabel.setText("✅ Checked In Successfully!");
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 16px;");
                slotInfoLabel.setText("Slot: A1 | Status: Occupied");
                checkoutBtn.setDisable(false);
            } else {
                showError(errorLabel, "Check-in failed. Try again.");
            }
            checkinBtn.setDisable(false);
            checkinBtn.setText("Check In");
        });

        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server");
            checkinBtn.setDisable(false);
            checkinBtn.setText("Check In");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleCheckout() {
        String bookingId = bookingIdField.getText().trim();

        checkoutBtn.setDisable(true);
        checkoutBtn.setText("Checking out...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = """
                    {"bookingId":"%s"}
                    """.formatted(bookingId);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/checkout"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Session.getInstance().getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

                HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> {
            statusLabel.setText("✅ Checked Out Successfully!");
            statusLabel.setStyle("-fx-text-fill: #1a73e8; -fx-font-size: 16px;");
            billLabel.setText("Total Bill: ₹50");
            billLabel.setVisible(true);
            checkoutBtn.setDisable(false);
            checkoutBtn.setText("Check Out");
        });

        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server");
            checkoutBtn.setDisable(false);
            checkoutBtn.setText("Check Out");
        });

        new Thread(task).start();
    }

    @FXML
    private void goToDashboard() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/dashboard.fxml")
            );
            Stage stage = (Stage) checkinBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}