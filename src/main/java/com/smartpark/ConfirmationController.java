package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.Base64;
import java.util.ResourceBundle;

public class ConfirmationController extends BaseController {

    @FXML private Label bookingIdLabel;
    @FXML private Label slotLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label amountLabel;
    @FXML private Button checkinBtn;
    @FXML private ImageView qrImageView;
    @FXML private Label qrStatusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int bookingId      = Session.getInstance().getBookingId();
        String slotNumber  = Session.getInstance().getSelectedSlotNumber();
        String slotType    = Session.getInstance().getSelectedSlotType();
        String vehiclePlate = Session.getInstance().getVehiclePlate();

        double rate = 50.0;
        if (slotType != null) switch (slotType.toUpperCase()) {
            case "BIKE"  -> rate = 20.0;
            case "TRUCK" -> rate = 100.0;
        }

        bookingIdLabel.setText("Booking ID: #" + bookingId);
        slotLabel.setText("Slot: " + (slotNumber != null ? slotNumber : "N/A")
                + " (" + (slotType != null ? slotType : "") + ")");
        vehicleLabel.setText("Vehicle: " + (vehiclePlate != null ? vehiclePlate : "N/A"));
        amountLabel.setText("Rate: ₹" + rate + "/hr (billed on checkout)");

        // Load QR code
        qrStatusLabel.setText("Generating QR code...");
        loadQRCode(bookingId);
    }

    private void loadQRCode(int bookingId) {
        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/booking/" + bookingId + "/qr"))
                        .GET().build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            try {
                // Extract base64 qrCode field
                String base64 = body.split("\"qrCode\":\"")[1].split("\"")[0];
                byte[] imageBytes = Base64.getDecoder().decode(base64);
                Image qrImage = new Image(new ByteArrayInputStream(imageBytes));
                qrImageView.setImage(qrImage);
                qrStatusLabel.setText("Scan QR code at entry gate");
            } catch (Exception ex) {
                qrStatusLabel.setText("QR code unavailable");
                System.out.println("QR parse error: " + ex.getMessage());
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() ->
            qrStatusLabel.setText("Could not load QR code")
        ));

        new Thread(task).start();
    }

    @FXML private void goToCheckin()   { loadPage("/com/smartpark/checkin.fxml",   900, 600, checkinBtn); }
    @FXML private void goToDashboard() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, checkinBtn); }
}