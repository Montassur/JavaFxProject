package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.example.MainApp;
import org.example.services.AuthService;
import org.example.entities.User;

public class LoginController {
    @FXML private TextField    emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label         messageLabel;

    // Grab the shared AuthService instance
    private final AuthService authService = MainApp.AUTH_SERVICE;

    @FXML
    private void handleLogin() {
        try {
            // Attempt login
            User u = authService
                    .login(emailField.getText(), passwordField.getText())
                    .orElseThrow(() -> new Exception("Invalid credentials"));

            // Block users in 'pending' state
            if (u.getRole() != null && ("" +
                    "pending").equals(u.getRole().getName())) {
                messageLabel.setText("Votre compte est en attente d'activation.");
                return;
            }

            // Admins get the back-office layout with sidebar
            if (u.getRole() != null && "admin".equals(u.getRole().getName())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackofficeLayout.fxml"));
                Parent root = loader.load();

                // Capture and initialize the LayoutController
                LayoutController layoutCtrl = loader.getController();
                MainApp.LAYOUT_CONTROLLER = layoutCtrl;
                layoutCtrl.setCurrentUser(u);
                layoutCtrl.onUserSet();

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("BackOffice");
            } else {
                // Regular users get their home screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserHome.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Bienvenue " + u.getPrenom());
            }
        } catch (Exception ex) {
            messageLabel.setText("Email ou mot de passe incorrect");
        }
    }

    @FXML
    private void goToSignup() throws Exception {
        Stage stage = (Stage) emailField.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Signup.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Inscription");
    }
}
