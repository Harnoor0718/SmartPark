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

public class BookingController extends BaseController {

    @FXML private ComboBox<String> vehicleTypeBox;
    @FXML private Label slotLabel;
    @FXML private TextField vehiclePlateField;
    @FXML private Button confirmBtn;
    @FXML private Label errorLabel;

    private int selectedSlotId = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        vehicleTypeBox.getItems().addAll("Car", "Bike", "Truck");
        vehicleTypeBox.setValue("Car");
        slotLabel.setText("Selected Slot: A" + selectedSlotId);
    }

    @FXML
    private void handleConfirmBooking() {
        String vehiclePlate = vehiclePlateField.getText().trim();
        String vehicleType = vehicleTypeBox.getValue();

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
            if (body.contains("bookingId") || body.contains("success")) {
                // Go to confirmation screen
                try {
                    Parent root = FXMLLoader.load(
                        getClass().getResource("/com/smartpark/confirmation.fxml")
                    );
                    Stage stage = (Stage) confirmBtn.getScene().getWindow();
                    stage.setScene(new Scene(root, 900, 600));
                } catch (Exception ex) {
                    showError(errorLabel, "Failed to load confirmation");
                }
            } else {
                showError(errorLabel, "Booking failed. Try again.");
                confirmBtn.setDisable(false);
                confirmBtn.setText("Confirm Booking");
            }
        });

        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server");
            confirmBtn.setDisable(false);
            confirmBtn.setText("Confirm Booking");
        });

        new Thread(task).start();
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/dashboard.fxml")
            );
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}