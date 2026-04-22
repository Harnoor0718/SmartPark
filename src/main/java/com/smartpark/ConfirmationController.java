package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationController extends BaseController {

    @FXML private Label bookingIdLabel;
    @FXML private Label slotLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label amountLabel;
    @FXML private Button checkinBtn;

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
    }

    @FXML private void goToCheckin()   { loadPage("/com/smartpark/checkin.fxml",   900, 600, checkinBtn); }
    @FXML private void goToDashboard() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, checkinBtn); }
}