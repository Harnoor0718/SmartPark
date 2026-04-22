package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        statusLabel.setText("");
        checkoutBtn.setDisable(true);

        int bookingId = Session.getInstance().getBookingId();
        if (bookingId > 0) {
            bookingIdField.setText(String.valueOf(bookingId));
            slotInfoLabel.setText("Slot: " + Session.getInstance().getSelectedSlotNumber()
                    + " | Vehicle: " + Session.getInstance().getVehiclePlate());
        } else {
            slotInfoLabel.setText("Enter booking ID to proceed");
        }
    }

    @FXML
    private void handleCheckin() {
        String text = bookingIdField.getText().trim();
        if (text.isEmpty()) { showError(errorLabel, "Please enter a booking ID"); return; }
        int bookingId;
        try { bookingId = Integer.parseInt(text); }
        catch (NumberFormatException e) { showError(errorLabel, "Booking ID must be a number"); return; }

        errorLabel.setVisible(false);
        checkinBtn.setDisable(true);
        checkinBtn.setText("Checking in...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/checkin"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"bookingId\":" + bookingId + "}")).build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            if (task.getValue().contains("\"status\":\"success\"")) {
                statusLabel.setText("✅ Checked In Successfully!");
                statusLabel.setStyle("-fx-text-fill:#4caf50;-fx-font-size:16px;-fx-font-weight:bold;");
                checkoutBtn.setDisable(false);
                checkinBtn.setDisable(true);
            } else {
                showError(errorLabel, "Check-in failed. Booking may already be active.");
            }
            checkinBtn.setText("Check In");
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            showError(errorLabel, "Cannot connect to server.");
            checkinBtn.setDisable(false); checkinBtn.setText("Check In");
        }));
        new Thread(task).start();
    }

    @FXML
    private void handleCheckout() {
        String text = bookingIdField.getText().trim();
        if (text.isEmpty()) { showError(errorLabel, "Please enter a booking ID"); return; }
        int bookingId;
        try { bookingId = Integer.parseInt(text); }
        catch (NumberFormatException e) { showError(errorLabel, "Booking ID must be a number"); return; }

        errorLabel.setVisible(false);
        checkoutBtn.setDisable(true);
        checkoutBtn.setText("Checking out...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/checkout"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"bookingId\":" + bookingId + "}")).build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            if (body.contains("\"status\":\"success\"")) {
                double amount = 0;
                try { amount = Double.parseDouble(body.split("\"totalAmount\":")[1].split("[,}]")[0].trim()); }
                catch (Exception ex) { System.out.println("Could not parse amount"); }
                statusLabel.setText("✅ Checked Out Successfully!");
                statusLabel.setStyle("-fx-text-fill:#1a2b4a;-fx-font-size:16px;-fx-font-weight:bold;");
                billLabel.setText("Total Bill: ₹" + amount);
                billLabel.setVisible(true);
                checkoutBtn.setDisable(true); checkoutBtn.setText("Check Out");
            } else {
                showError(errorLabel, "Check-out failed. Please check in first.");
                checkoutBtn.setDisable(false); checkoutBtn.setText("Check Out");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            showError(errorLabel, "Cannot connect to server.");
            checkoutBtn.setDisable(false); checkoutBtn.setText("Check Out");
        }));
        new Thread(task).start();
    }

    @FXML private void goToDashboard() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, checkinBtn); }
}