package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    // 🔥 ENTER KEY SUPPORT (like HTML)
    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    // 🔐 LOGIN LOGIC
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

        // ✅ SUCCESS
        task.setOnSucceeded(e -> {
            String body = task.getValue();

            try {
                if (body.contains("token")) {
                    // extract token
                    String token = body.split("\"token\":\"")[1].split("\"")[0];

                    Session.getInstance().setToken(token);
                    Session.getInstance().setUsername(email);

                    loadPage("/com/smartpark/dashboard.fxml");
                } else {
                    showError(errorLabel, "Invalid email or password");
                    resetButton();
                }
            } catch (Exception ex) {
                showError(errorLabel, "Login failed (invalid response)");
                resetButton();
            }
        });

        // ❌ FAILED
        task.setOnFailed(e -> {
            showError(errorLabel, "Cannot connect to server");
            resetButton();
        });

        new Thread(task).start();
    }

    // 🔄 RESET BUTTON
    private void resetButton() {
        loginBtn.setDisable(false);
        loginBtn.setText("Login");
    }

    // 🔁 NAVIGATION METHOD (Reusable)
    private void loadPage(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception e) {
            showError(errorLabel, "Failed to load page");
        }
    }

    // 🔗 GO TO REGISTER PAGE
    @FXML
    private void goToRegister() {
        loadPage("/com/smartpark/register.fxml");
    }
}