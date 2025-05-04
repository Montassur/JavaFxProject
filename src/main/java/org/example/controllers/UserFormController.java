package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.MainApp;
import org.example.entities.User;
import org.example.entities.Role;
import org.example.services.UserServices;
import org.example.dao.RoleDAO;
import org.example.utils.MyDataBase;

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
    private ComboBox<Role> roleComboBox;              // now Role
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
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        telephoneField.setText(user.getNumeroTelephone());
        roleComboBox.setValue(user.getRole());
        motDePasseField.setText(user.getMotDePasse());
        confirmerMotDePasseField.setText(user.getConfirmerMotDePasse());
        activeCheckBox.setSelected(user.isActive());

        ajouterBtn.setVisible(false);
        modifierBtn.setVisible(true);
    }

    @FXML
    private void ajouterUtilisateur(ActionEvent event) {
        try {
            if (champVide(nomField, "Nom") ||
                    champVide(prenomField, "Prénom") ||
                    champVide(emailField, "Email") ||
                    champVide(telephoneField, "Téléphone") ||
                    champVidePassword(motDePasseField, "Mot de passe") ||
                    champVidePassword(confirmerMotDePasseField, "Confirmer mot de passe") ||
                    champCombo(roleComboBox, "Rôle")) {
                return;
            }

            if (!motDePasseField.getText().equals(confirmerMotDePasseField.getText())) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas !");
                return;
            }

            if (userToEdit == null) {
                User user = new User(
                        nomField.getText(),
                        prenomField.getText(),
                        emailField.getText(),
                        telephoneField.getText(),
                        roleComboBox.getValue(),
                        motDePasseField.getText(),
                        confirmerMotDePasseField.getText(),
                        activeCheckBox.isSelected()
                );
                userServices.ajouter(user);
            } else {
                userToEdit.setNom(nomField.getText());
                userToEdit.setPrenom(prenomField.getText());
                userToEdit.setEmail(emailField.getText());
                userToEdit.setNumeroTelephone(telephoneField.getText());
                userToEdit.setRole(roleComboBox.getValue());
                userToEdit.setMotDePasse(motDePasseField.getText());
                userToEdit.setConfirmerMotDePasse(confirmerMotDePasseField.getText());
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