package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.entities.User;
import org.example.services.UserServices;

import java.util.List;

public class DashboardController {
    @FXML private Label totalUsersLabel;
    @FXML private Label activeUsersLabel;

    private final UserServices userServices = new UserServices();

    @FXML
    public void initialize() {
        try {
            List<User> users = userServices.getAll();
            long total  = users.size();
            long active = users.stream().filter(User::isActive).count();

            totalUsersLabel.setText(String.valueOf(total));
            activeUsersLabel.setText(String.valueOf(active));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
