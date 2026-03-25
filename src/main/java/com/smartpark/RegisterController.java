package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends BaseController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            showError(errorLabel, "All fields are required");
            return;
        }
        // TODO: connect to /api/auth/register
        System.out.println("Register clicked");
    }

    @FXML
    private void goToLogin() {
        // TODO: navigate back to login screen
    }
}