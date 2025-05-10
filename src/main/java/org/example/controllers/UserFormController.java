package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import org.example.MainApp;
import org.example.entities.User;
import org.example.entities.Role;
import org.example.services.UserServices;
import org.example.dao.RoleDAO;
import org.example.utils.MyDataBase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.util.List;

public class UserFormController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private ComboBox<Role> roleComboBox; // now Role
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private PasswordField confirmerMotDePasseField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private Button ajouterBtn;
    @FXML
    private Button modifierBtn;

    private final UserServices userServices = new UserServices();
    private User userToEdit = null;
    private String existingPasswordHash;

    @FXML
    public void initialize() {
        try {
            Connection conn = MyDataBase.getInstance().getConnection();
            List<Role> roles = new RoleDAO(conn).findAll();
            roleComboBox.setItems(FXCollections.observableArrayList(roles));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les rôles depuis la base.");
        }
        ajouterBtn.setVisible(true);
        modifierBtn.setVisible(false);
    }

    public void setUserToEdit(User user) {
        this.userToEdit = user;
        this.existingPasswordHash = user.getMotDePasse(); // Save the current password hash
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        telephoneField.setText(user.getNumeroTelephone());
        roleComboBox.setValue(user.getRole());

        // Leave the password fields empty so the user can choose to change the password
        motDePasseField.setText(""); // Leave empty for editing
        confirmerMotDePasseField.setText(""); // Leave empty for editing
        activeCheckBox.setSelected(user.isActive());

        ajouterBtn.setVisible(false);
        modifierBtn.setVisible(true);
    }

    @FXML
    private void ajouterUtilisateur(ActionEvent event) {
        try {
            // Validate fields
            if (champVide(nomField, "Nom") ||
                    champVide(prenomField, "Prénom") ||
                    champVide(emailField, "Email") ||
                    champVide(telephoneField, "Téléphone") ||
                    champVidePassword(motDePasseField, "Mot de passe") ||
                    champVidePassword(confirmerMotDePasseField, "Confirmer mot de passe") ||
                    champCombo(roleComboBox, "Rôle")) {
                return;
            }

            // Validate password confirmation
            if (!motDePasseField.getText().equals(confirmerMotDePasseField.getText())) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas !");
                return;
            }

            // Hash the password only if modified
            String hash;
            if (!motDePasseField.getText().isEmpty()) {
                hash = BCrypt.hashpw(motDePasseField.getText(), BCrypt.gensalt());
            } else {
                hash = existingPasswordHash;  // Keep existing hashed password if not modified
            }

            if (userToEdit == null) {
                // Adding new user
                User user = new User(
                        nomField.getText(),
                        prenomField.getText(),
                        emailField.getText(),
                        telephoneField.getText(),
                        roleComboBox.getValue(),
                        hash, // store hashed password
                        hash, // store hashed password
                        activeCheckBox.isSelected()
                );
                userServices.ajouter(user);
            } else {
                // Updating existing user
                userToEdit.setNom(nomField.getText());
                userToEdit.setPrenom(prenomField.getText());
                userToEdit.setEmail(emailField.getText());
                userToEdit.setNumeroTelephone(telephoneField.getText());
                userToEdit.setRole(roleComboBox.getValue());

                // If the password field is not empty, hash the new password
                if (!motDePasseField.getText().isEmpty()) {
                    userToEdit.setMotDePasse(BCrypt.hashpw(motDePasseField.getText(), BCrypt.gensalt()));
                    userToEdit.setConfirmerMotDePasse(confirmerMotDePasseField.getText());
                } else {
                    // If password is not changed, use the existing hashed password
                    userToEdit.setMotDePasse(existingPasswordHash);
                    userToEdit.setConfirmerMotDePasse(existingPasswordHash);
                }

                userToEdit.setActive(activeCheckBox.isSelected());
                userServices.modifier(userToEdit);
            }

            // Return to user list within the layout
            MainApp.LAYOUT_CONTROLLER.loadContent("/UserList.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de l'enregistrement : " + e.getMessage());
        }
    }

    private boolean champVide(TextInputControl champ, String nomChamp) {
        if (champ.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez remplir le champ : " + nomChamp);
            champ.requestFocus();
            return true;
        }
        return false;
    }

    private boolean champVidePassword(PasswordField champ, String nomChamp) {
        if (champ.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez remplir le champ : " + nomChamp);
            champ.requestFocus();
            return true;
        }
        return false;
    }

    private boolean champCombo(ComboBox<Role> combo, String nomChamp) {
        if (combo.getValue() == null) {
            showAlert("Champ requis", "Veuillez sélectionner le champ : " + nomChamp);
            combo.requestFocus();
            return true;
        }
        return false;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // No swapping of Scene: use layout controller
    @FXML
    private void retourVersListe(ActionEvent event) {
        MainApp.LAYOUT_CONTROLLER.loadContent("/UserList.fxml");
    }
}
