package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import org.example.MainApp;
import org.example.entities.User;
import org.example.services.UserServices;

import java.util.List;

public class UserListController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> nomCol;
    @FXML private TableColumn<User, String> prenomCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> telephoneCol;
    @FXML private TableColumn<User, String> roleCol;
    @FXML private TableColumn<User, String> statutCol;
    @FXML private TableColumn<User, Void> actionCol;

    private final UserServices userServices = new UserServices();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject()
        );
        nomCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getNom())
        );
        prenomCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom())
        );
        emailCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail())
        );
        telephoneCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getNumeroTelephone())
        );
        roleCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getRole() != null
                                ? c.getValue().getRole().getName()
                                : ""
                )
        );
        statutCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    boolean actif = getTableRow().getItem().isActive();
                    setText(actif ? "Actif" : "Inactif");
                    setStyle("-fx-text-fill: " + (actif ? "green" : "red") + "; -fx-font-weight: bold;");
                }
            }
        });

        afficherUtilisateurs();
        ajouterBoutonsAction();
    }

    public void afficherUtilisateurs() {
        try {
            List<User> users = userServices.getAll();
            ObservableList<User> list = FXCollections.observableArrayList(users);
            userTable.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ajouterBoutonsAction() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btnModifier  = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox hbox            = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");

                btnModifier.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserForm.fxml"));
                        Node form = loader.load();
                        // initialize form controller
                        UserFormController ctrl = loader.getController();
                        ctrl.setUserToEdit(user);
                        // inject into sidebar layout
                        MainApp.LAYOUT_CONTROLLER.loadContentNode(form);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                btnSupprimer.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Voulez-vous vraiment supprimer cet utilisateur ?",
                            ButtonType.OK, ButtonType.CANCEL);
                    alert.showAndWait().ifPresent(resp -> {
                        if (resp == ButtonType.OK) {
                            try {
                                userServices.supprimer(user.getId());
                                getTableView().getItems().remove(user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });

                hbox.setStyle("-fx-alignment: CENTER;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
    @FXML
    private void goToAddUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserForm.fxml"));
            Node form = loader.load();
            MainApp.LAYOUT_CONTROLLER.loadContentNode(form);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
