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
    private void handleLogin() {
        String email = usernameField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError(errorLabel, "Please fill in all fields");
            return;
        }

        loginBtn.setDisable(true);
        loginBtn.setText("Logging in...");

        javafx.concurrent.Task<String> task = new javafx.concurrent.Task<>() {
            @Override
            protected String call() throws Exception {
                String json = """
                    {"email":"%s","password":"%s"}
                    """.formatted(email, password);

                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
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
            if (body.contains("token")) {
                // Extract token
                String token = body.split("\"token\":\"")[1].split("\"")[0];
                Session.getInstance().setToken(token);
                Session.getInstance().setUsername(email);

                // Go to dashboard
                try {
                    Parent root = FXMLLoader.load(
                        getClass().getResource("/com/smartpark/dashboard.fxml")
                    );
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.setScene(new Scene(root, 900, 600));
                } catch (Exception ex) {
                    showError(errorLabel, "Failed to load dashboard");
                }
            } else {
                showError(errorLabel, "Invalid email or password");
                loginBtn.setDisable(false);
                loginBtn.setText("Login");
            }
        });

        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server");
            loginBtn.setDisable(false);
            loginBtn.setText("Login");
        });

        new Thread(task).start();
    }
}