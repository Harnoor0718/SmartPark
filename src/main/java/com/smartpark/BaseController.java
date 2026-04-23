package com.smartpark;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.net.http.HttpClient;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {

    protected HttpClient httpClient = HttpClient.newHttpClient();
    protected static final String BASE_URL = "http://localhost:8080/api";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    protected void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    protected void loadPage(String fxmlPath, int width, int height, Node anyNode) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) anyNode.getScene().getWindow();
            stage.getScene().setRoot(root);
            Platform.runLater(() -> {
                stage.toFront();
                stage.setIconified(false);
                stage.requestFocus();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}