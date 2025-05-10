package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.User;
import org.example.entities.Role;

public class LayoutController {

    @FXML private VBox sidebar;
    @FXML private Button btnDashboard, btnUsers, btnTickets, btnReclamations, btnSponsors;

    @FXML private StackPane contentPane;

    // Nouveaux éléments pour le header
    @FXML private Label userNameLabel;
    @FXML private MenuButton profileMenu;

    private User currentUser;

    /** Appelée une fois que l'utilisateur est injecté */
    protected void onUserSet() {
        Role role = currentUser.getRole();
        String name = role != null ? role.getName() : "pending";

        userNameLabel.setText(currentUser.getPrenom() + " " + currentUser.getNom());

        btnDashboard.setVisible(true);
        btnUsers.setVisible("admin".equals(name));
        btnTickets.setVisible("admin".equals(name) || "gestionnaire_tickets".equals(name));
        btnReclamations.setVisible("admin".equals(name) || "agent_reclamation".equals(name));
        btnSponsors.setVisible("admin".equals(name) || "responsable_sponsor".equals(name));

        loadContent("/Dashboard.fxml");
    }

    // Navigation
    @FXML private void goDashboard()    { loadContent("/Dashboard.fxml"); }
    @FXML private void goUsers()        { loadContent("/UserList.fxml"); }
    @FXML private void goTickets()      { loadContent("/Tickets.fxml"); }
    @FXML private void goReclamations() { loadContent("/Reclamations.fxml"); }
    @FXML private void goSponsors()     { loadContent("/Sponsors.fxml"); }

    // 🔁 Nouveau : ouvrir la page de profil
    @FXML private void goProfile() {
        loadContent("/Profile.fxml");
    }

    // Déconnexion
    @FXML private void logout() {
        try {
            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
            stage.setTitle("Connexion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Injecter contenu dynamique
    public void loadContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadContentNode(Node node) {
        contentPane.getChildren().setAll(node);
    }

    // Setter utilisateur
    public void setCurrentUser(User user) {
        this.currentUser = user;
        onUserSet();
    }

    // Getter pour autres contrôleurs
    public User getCurrentUser() {
        return currentUser;
    }
}
