package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceiptController extends BaseController {

    @FXML private Label receiptIdLabel;
    @FXML private Label receiptBookingLabel;
    @FXML private Label receiptVehicleLabel;
    @FXML private Label receiptHoursLabel;
    @FXML private Label receiptMethodLabel;
    @FXML private Label receiptAmountLabel;
    @FXML private Button dashBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int paymentId  = Session.getInstance().getPaymentId();
        int bookingId  = Session.getInstance().getBookingId();
        String plate   = Session.getInstance().getVehiclePlate();
        double amount  = Session.getInstance().getTotalAmount();
        double hours   = Session.getInstance().getHoursParked();
        String method  = Session.getInstance().getPaymentMethod();

        receiptIdLabel.setText("Payment ID: #" + paymentId);
        receiptBookingLabel.setText("Booking ID: #" + bookingId);
        receiptVehicleLabel.setText("Vehicle: " + (plate != null ? plate : "N/A"));
        receiptHoursLabel.setText("Hours Parked: " + String.format("%.1f", hours) + " hr");
        receiptMethodLabel.setText("Payment Method: " + (method != null ? method : "N/A"));
        receiptAmountLabel.setText("Amount Paid: ₹" + amount);
    }

    @FXML private void goToDashboard() { loadPage("/com/smartpark/dashboard.fxml", 900, 600, dashBtn); }
    @FXML private void goToHistory()   { loadPage("/com/smartpark/history.fxml",   900, 600, dashBtn); }
}