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
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError(errorLabel, "All fields are required");
            return;
        }

        if (!email.contains("@")) {
            showError(errorLabel, "Please enter a valid email address");
            return;
        }

        if (password.length() < 4) {
            showError(errorLabel, "Password must be at least 4 characters");
            return;
        }

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = """
                    {"username":"%s","email":"%s","password":"%s","role":"customer"}
                    """.formatted(name, email, password);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/auth/register"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString()
                );
                return response.body();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            String body = task.getValue();
            if (body.contains("\"status\":\"success\"")) {
                // Registration successful — go to login
                loadPage("/com/smartpark/login.fxml", 800, 600);
            } else if (body.contains("already exists")) {
                showError(errorLabel, "Email already registered. Please login.");
            } else {
                showError(errorLabel, "Registration failed. Please try again.");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() ->
            showError(errorLabel, "Cannot connect to server. Is Spring Boot running?")
        ));

        new Thread(task).start();
    }

    @FXML
    private void goToLogin() {
        loadPage("/com/smartpark/login.fxml", 800, 600);
    }

    private void loadPage(String fxmlPath, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}