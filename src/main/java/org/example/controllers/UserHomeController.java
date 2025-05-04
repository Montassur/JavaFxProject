package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserHomeController extends HomeControllerBase {
    @FXML private Label welcomeLabel;

    @Override protected void onUserSet() {
        welcomeLabel.setText("Bienvenue " + currentUser.getPrenom());
    }
}