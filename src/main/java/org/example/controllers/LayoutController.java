package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.example.entities.User;
import org.example.entities.Role;

public class LayoutController {

    @FXML private VBox    sidebar;
    @FXML private Button  btnDashboard, btnUsers, btnTickets, btnReclamations, btnSponsors, btnLogout;
    @FXML private StackPane contentPane;

    private User currentUser;

    /** Called once currentUser is set to configure UI and load default view */
    protected void onUserSet() {
        Role role = currentUser.getRole();
        String name = role != null ? role.getName() : "pending";

        btnDashboard .setVisible(true);
        btnLogout    .setVisible(true);
        btnUsers     .setVisible("admin".equals(name));
        btnTickets   .setVisible("admin".equals(name) || "gestionnaire_tickets".equals(name));
        btnReclamations.setVisible("admin".equals(name) || "agent_reclamation".equals(name));
        btnSponsors  .setVisible("admin".equals(name) || "responsable_sponsor".equals(name));

        // load dashboard by default
        loadContent("/Dashboard.fxml");
    }

    @FXML private void goDashboard()    { loadContent("/Dashboard.fxml"); }
    @FXML private void goUsers()        { loadContent("/UserList.fxml"); }
    @FXML private void goTickets()      { loadContent("/Tickets.fxml"); }
    @FXML private void goReclamations() { loadContent("/Reclamations.fxml"); }
    @FXML private void goSponsors()     { loadContent("/Sponsors.fxml"); }
    @FXML private void logout() {
        try {
            Stage st = (Stage) sidebar.getScene().getWindow();
            st.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Login.fxml"))));
            st.setTitle("Connexion");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * PUBLIC: load an FXML into the center pane
     */
    public void loadContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(node);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * PUBLIC: inject an already-loaded Node into the center pane
     */
    public void loadContentNode(Node node) {
        contentPane.getChildren().setAll(node);
    }

    /** Sets the logged-in user and triggers initial UI setup */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        onUserSet();
    }
}
