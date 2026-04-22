package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class BookingController extends BaseController {

    @FXML private ComboBox<String> vehicleTypeBox;
    @FXML private Label slotLabel;
    @FXML private TextField vehiclePlateField;
    @FXML private Button confirmBtn;
    @FXML private Label errorLabel;

    private int selectedSlotId;
    private String selectedSlotNumber;
    private String selectedSlotType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        selectedSlotId     = Session.getInstance().getSelectedSlotId();
        selectedSlotNumber = Session.getInstance().getSelectedSlotNumber();
        selectedSlotType   = Session.getInstance().getSelectedSlotType();

        vehicleTypeBox.getItems().addAll("Car", "Bike", "Truck");
        if (selectedSlotType != null) {
            switch (selectedSlotType.toUpperCase()) {
                case "BIKE"  -> vehicleTypeBox.setValue("Bike");
                case "TRUCK" -> vehicleTypeBox.setValue("Truck");
                default      -> vehicleTypeBox.setValue("Car");
            }
        } else vehicleTypeBox.setValue("Car");

        slotLabel.setText("Selected Slot: " + (selectedSlotNumber != null ? selectedSlotNumber : "N/A")
                + " (" + (selectedSlotType != null ? selectedSlotType : "") + ")");
    }

    @FXML
    private void handleConfirmBooking() {
        String vehiclePlate = vehiclePlateField.getText().trim();
        if (vehiclePlate.isEmpty()) { showError(errorLabel, "Please enter vehicle plate number"); return; }

        confirmBtn.setDisable(true);
        confirmBtn.setText("Booking...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = "{\"userId\":%d,\"slotId\":%d,\"vehiclePlate\":\"%s\"}"
                        .formatted(Session.getInstance().getUserId(), selectedSlotId, vehiclePlate);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/bookings"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json)).build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> {
            String body = task.getValue();
            if (body.contains("\"status\":\"success\"")) {
                try {
                    int bookingId = Integer.parseInt(body.split("\"bookingId\":")[1].split("[,}]")[0].trim());
                    Session.getInstance().setBookingId(bookingId);
                    Session.getInstance().setVehiclePlate(vehiclePlate);
                    Platform.runLater(() -> loadPage("/com/smartpark/confirmation.fxml", 900, 600, confirmBtn));
                } catch (Exception ex) {
                    Platform.runLater(() -> showError(errorLabel, "Booking done but failed to load confirmation"));
                }
            } else {
                Platform.runLater(() -> { showError(errorLabel, "Booking failed. Slot may be occupied."); resetBtn(); });
            }
        });

        task.setOnFailed(e -> Platform.runLater(() -> { showError(errorLabel, "Cannot connect to server."); resetBtn(); }));
        new Thread(task).start();
    }

    private void resetBtn() { confirmBtn.setDisable(false); confirmBtn.setText("Confirm Booking"); }

    @FXML private void goBack() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, confirmBtn); }
}