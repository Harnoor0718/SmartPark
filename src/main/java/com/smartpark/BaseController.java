package com.smartpark;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.net.http.HttpClient;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {

    protected HttpClient httpClient = HttpClient.newHttpClient();
    protected static final String BASE_URL = "http://localhost:8080/api";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // subclasses override this — no super call needed
    }

    protected void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    // Single shared loadPage — pass any node that's part of the scene
    protected void loadPage(String fxmlPath, int width, int height, Node anyNode) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) anyNode.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}