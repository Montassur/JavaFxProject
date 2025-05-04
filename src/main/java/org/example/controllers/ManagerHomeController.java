package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ManagerHomeController extends HomeControllerBase {
    @FXML private Label welcomeLabel;

    @Override protected void onUserSet() {
        welcomeLabel.setText("Bonjour Manager " + currentUser.getPrenom());
        // manager UI
    }
}