package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import org.example.MainApp;
import org.example.services.AuthService;
import org.example.entities.User;

public class SignupController {
    @FXML private TextField     nomField;
    @FXML private TextField     prenomField;
    @FXML private TextField     emailField;
    @FXML private TextField     phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label         messageLabel;

    // Grab the shared AuthService that MainApp initialized
    private final AuthService authService = MainApp.AUTH_SERVICE;

    @FXML
    private void handleSignup() {
        // 1) Validate email & phone
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            messageLabel.setText("Adresse email invalide");
            return;
        }
        if (!phoneField.getText().matches("\\d{8}")) {
            messageLabel.setText("Téléphone : 8 chiffres");
            return;
        }
        // 2) Validate passwords match
        if (!passwordField.getText().equals(confirmField.getText())) {
            messageLabel.setText("Les mots de passe ne correspondent pas");
            return;
        }

        try {
            // Create user with role=null (pending) and active=false
            User u = new User(
                    nomField.getText(),
                    prenomField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    null,
                    passwordField.getText(),
                    confirmField.getText(),
                    false
            );
            // THIS WAS NULL before — now it comes from MainApp
            authService.signup(u);

            // On success, go back to login
            Stage st = (Stage) nomField.getScene().getWindow();
            st.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/Login.fxml"))
            ));
            st.setTitle("Connexion");
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
}
