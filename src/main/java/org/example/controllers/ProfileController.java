package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.MainApp;
import org.example.entities.User;
import org.example.services.UserServices;
import org.mindrot.jbcrypt.BCrypt;

public class ProfileController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserServices userServices = new UserServices();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = MainApp.LAYOUT_CONTROLLER.getCurrentUser();
        if (currentUser != null) {
            nomField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            emailField.setText(currentUser.getEmail());
            telephoneField.setText(currentUser.getNumeroTelephone());
        }
    }

    @FXML
    private void handleSave() {
        try {
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setEmail(emailField.getText());
            currentUser.setNumeroTelephone(telephoneField.getText());

            if (!passwordField.getText().isEmpty()) {
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    showAlert("Erreur", "Les mots de passe ne correspondent pas.");
                    return;
                }
                String hashed = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());
                currentUser.setMotDePasse(hashed);
                currentUser.setConfirmerMotDePasse(hashed);
            }

            userServices.modifier(currentUser);
            showAlert("Succès", "Profil mis à jour !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de mettre à jour : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        MainApp.LAYOUT_CONTROLLER.loadContent("/Dashboard.fxml");
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
