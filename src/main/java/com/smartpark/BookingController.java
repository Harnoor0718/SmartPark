package com.smartpark;

import javafx.application.Platform;
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

        // Get slot info from Session
        selectedSlotId = Session.getInstance().getSelectedSlotId();
        selectedSlotNumber = Session.getInstance().getSelectedSlotNumber();
        selectedSlotType = Session.getInstance().getSelectedSlotType();

        vehicleTypeBox.getItems().addAll("Car", "Bike", "Truck");

        // Pre-select based on slot type
        if (selectedSlotType != null) {
            switch (selectedSlotType.toUpperCase()) {
                case "BIKE" -> vehicleTypeBox.setValue("Bike");
                case "TRUCK" -> vehicleTypeBox.setValue("Truck");
                default -> vehicleTypeBox.setValue("Car");
            }
        } else {
            vehicleTypeBox.setValue("Car");
        }

        slotLabel.setText("Selected Slot: " + (selectedSlotNumber != null ? selectedSlotNumber : "N/A")
                + " (" + (selectedSlotType != null ? selectedSlotType : "") + ")");
    }

    @FXML
    private void handleConfirmBooking() {
        String vehiclePlate = vehiclePlateField.getText().trim();

        if (vehiclePlate.isEmpty()) {
            showError(errorLabel, "Please enter vehicle plate number");
            return;
        }

        confirmBtn.setDisable(true);
        confirmBtn.setText("Booking...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = """
                    {"userId":%d,"slotId":%d,"vehiclePlate":"%s"}
                    """.formatted(
                        Session.getInstance().getUserId(),
                        selectedSlotId,
                        vehiclePlate
                    );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/bookings"))
                        .header("Content-Type", "application/json")
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
            if (body.contains("\"status\":\"success\"")) {
                try {
                    // Parse bookingId from response
                    String bookingIdStr = body.split("\"bookingId\":")[1].split("[,}]")[0].trim();
                    int bookingId = Integer.parseInt(bookingIdStr);

                    // Store bookingId in session for confirmation page
                    Session.getInstance().setBookingId(bookingId);
                    Session.getInstance().setVehiclePlate(vehiclePlate);

                    Platform.runLater(() -> loadPage("/com/smartpark/confirmation.fxml", 900, 600));

                } catch (Exception ex) {
                    Platform.runLater(() -> showError(errorLabel, "Booking done but failed to load confirmation"));
                }
            } else {
                Platform.runLater(() -> {
                    showError(errorLabel, "Booking failed. Slot may already be occupied.");
                    confirmBtn.setDisable(false);
                    confirmBtn.setText("Confirm Booking");
                });
            }
        });

        task.setOnFailed(e -> Platform.runLater(() -> {
            showError(errorLabel, "Cannot connect to server. Is Spring Boot running?");
            confirmBtn.setDisable(false);
            confirmBtn.setText("Confirm Booking");
        }));

        new Thread(task).start();
    }

    @FXML
    private void goBack() {
        loadPage("/com/smartpark/dashboard.fxml", 900, 600);
    }

    private void loadPage(String fxmlPath, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}