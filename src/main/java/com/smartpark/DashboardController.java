package com.smartpark;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Refreshable {

    @FXML private Label userLabel;
    @FXML private FlowPane slotGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLabel.setText("Welcome back!");
        // Day 8: load slots here
    }

    @Override
    public void refresh() {
        // Day 15: auto-refresh every 5 seconds
    }

    @FXML
    private void handleLogout() {
        Session.getInstance().setToken(null);
        System.out.println("Logged out");
    }
}