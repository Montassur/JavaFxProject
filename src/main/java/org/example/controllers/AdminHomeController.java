package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHomeController extends HomeControllerBase {
    @FXML private Label welcomeLabel;

    @Override protected void onUserSet() {
        welcomeLabel.setText("Bonjour Admin " + currentUser.getPrenom());
        // Load admin-specific UI here
    }
}