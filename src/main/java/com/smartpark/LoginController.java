package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.ResourceBundle;

public class LoginController extends BaseController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) handleLogin();
    }

    @FXML
    private void handleLogin() {
        String email = usernameField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        if (email.isEmpty() || password.isEmpty()) {
            showError(errorLabel, "Please enter email and password!");
            return;
        }

        loginBtn.setDisable(true);
        loginBtn.setText("Logging in...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            }
        };

        task.setOnSucceeded(e -> {
            String body = task.getValue();
            if (body.contains("\"status\":\"success\"")) {
                try {
                    int userId = Integer.parseInt(body.split("\"userId\":")[1].split("[,}]")[0].trim());
                    String username = body.split("\"username\":\"")[1].split("\"")[0];
                    String role = body.split("\"role\":\"")[1].split("\"")[0];

                    Session.getInstance().setUserId(userId);
                    Session.getInstance().setUsername(username);
                    Session.getInstance().setRole(role);

                    loadPage("/com/smartpark/dashboard.fxml", 900, 600, loginBtn);
                } catch (Exception ex) {
                    showError(errorLabel, "Login failed — unexpected response");
                    resetButton();
                }
            } else {
                showError(errorLabel, "Invalid email or password");
                resetButton();
            }
        });

        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server. Is Spring Boot running?");
            resetButton();
        });

        new Thread(task).start();
    }

    private void resetButton() {
        loginBtn.setDisable(false);
        loginBtn.setText("Login");
    }

    @FXML
    private void goToRegister() {
        loadPage("/com/smartpark/register.fxml", 900, 600, loginBtn);
    }
}