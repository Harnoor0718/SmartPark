package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class PaymentController extends BaseController {

    @FXML private Label bookingIdLabel;
    @FXML private Label hoursLabel;
    @FXML private Label rateLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label selectedMethodLabel;
    @FXML private Label errorLabel;
    @FXML private Button payBtn;
    @FXML private Button cashBtn;
    @FXML private Button cardBtn;
    @FXML private Button upiBtn;

    private String selectedMethod = null;
    private double totalAmount = 0;
    private double hoursParked = 0;

    private static final String SELECTED_STYLE =
        "-fx-background-color:#1a2b4a;-fx-text-fill:white;" +
        "-fx-font-size:14px;-fx-font-weight:bold;-fx-padding:14;" +
        "-fx-background-radius:8;-fx-cursor:hand;";

    private static final String NORMAL_STYLE =
        "-fx-background-color:#f0f2f5;-fx-text-fill:#1a2b4a;" +
        "-fx-font-size:14px;-fx-font-weight:bold;-fx-padding:14;" +
        "-fx-background-radius:8;-fx-border-color:#ddd;" +
        "-fx-border-radius:8;-fx-cursor:hand;";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);

        int bookingId = Session.getInstance().getBookingId();
        totalAmount   = Session.getInstance().getTotalAmount();
        hoursParked   = Session.getInstance().getHoursParked();
        String plate  = Session.getInstance().getVehiclePlate();
        String slotType = Session.getInstance().getSelectedSlotType();

        double rate = 50.0;
        if (slotType != null) switch (slotType.toUpperCase()) {
            case "BIKE"  -> rate = 20.0;
            case "TRUCK" -> rate = 100.0;
        }

        bookingIdLabel.setText("Booking ID: #" + bookingId + "  |  Vehicle: " + (plate != null ? plate : "N/A"));
        hoursLabel.setText("Hours Parked: " + String.format("%.1f", hoursParked) + " hr");
        rateLabel.setText("Rate: ₹" + rate + "/hr");
        totalAmountLabel.setText("Total Amount: ₹" + totalAmount);
    }

    @FXML
    private void selectCash() {
        selectedMethod = "CASH";
        cashBtn.setStyle(SELECTED_STYLE);
        cardBtn.setStyle(NORMAL_STYLE);
        upiBtn.setStyle(NORMAL_STYLE);
        selectedMethodLabel.setText("✅ Cash selected");
        selectedMethodLabel.setStyle("-fx-text-fill:#4caf50;-fx-font-size:12px;");
    }

    @FXML
    private void selectCard() {
        selectedMethod = "CARD";
        cardBtn.setStyle(SELECTED_STYLE);
        cashBtn.setStyle(NORMAL_STYLE);
        upiBtn.setStyle(NORMAL_STYLE);
        selectedMethodLabel.setText("✅ Card selected");
        selectedMethodLabel.setStyle("-fx-text-fill:#4caf50;-fx-font-size:12px;");
    }

    @FXML
    private void selectUpi() {
        selectedMethod = "UPI";
        upiBtn.setStyle(SELECTED_STYLE);
        cashBtn.setStyle(NORMAL_STYLE);
        cardBtn.setStyle(NORMAL_STYLE);
        selectedMethodLabel.setText("✅ UPI selected");
        selectedMethodLabel.setStyle("-fx-text-fill:#4caf50;-fx-font-size:12px;");
    }

    @FXML
    private void handlePayment() {
        if (selectedMethod == null) {
            showError(errorLabel, "Please select a payment method!");
            return;
        }

        payBtn.setDisable(true);
        payBtn.setText("Processing...");
        errorLabel.setVisible(false);

        int bookingId = Session.getInstance().getBookingId();

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = "{\"bookingId\":%d,\"amount\":%.1f,\"paymentMethod\":\"%s\",\"hoursParked\":%.1f}"
                        .formatted(bookingId, totalAmount, selectedMethod, hoursParked);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/payments"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json)).build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            if (body.contains("\"status\":\"success\"")) {
                try {
                    String paymentId = body.split("\"paymentId\":")[1].split("[,}]")[0].trim();
                    Session.getInstance().setPaymentId(Integer.parseInt(paymentId));
                } catch (Exception ex) { System.out.println("Could not parse paymentId"); }
                loadPage("/com/smartpark/receipt.fxml", 900, 600, payBtn);
            } else {
                showError(errorLabel, "Payment failed. Please try again.");
                payBtn.setDisable(false);
                payBtn.setText("Pay Now");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            showError(errorLabel, "Cannot connect to server.");
            payBtn.setDisable(false);
            payBtn.setText("Pay Now");
        }));

        new Thread(task).start();
    }
}