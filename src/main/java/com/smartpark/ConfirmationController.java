package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
        // Show booking details
        bookingIdLabel.setText("Booking ID: #BK001");
        slotLabel.setText("Slot: A1");
        vehicleLabel.setText("Vehicle: PB-01-AB-1234");
        amountLabel.setText("Amount: ₹50");
    }

    @FXML
    private void goToCheckin() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/smartpark/checkin.fxml")
            );
            Stage stage = (Stage) checkinBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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