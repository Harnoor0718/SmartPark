package com.smartpark;

import javafx.fxml.Initializable;
import java.net.http.HttpClient;

public abstract class BaseController implements Initializable {

    protected HttpClient httpClient = HttpClient.newHttpClient();
    protected static final String BASE_URL = "http://localhost:8080/api";

    protected void showError(javafx.scene.control.Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}