package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserDetailsController {

    @FXML
    private TextField ageDisplay;

    @FXML
    private TextField prenomDisplay;

    @FXML
    private TextField nomDisplay;

    public void setNomDisplay(String nomDisplay) {
        this.nomDisplay.setText(nomDisplay);
    }


    public void setPrenomDisplay(String prenomDisplay) {
        this.prenomDisplay.setText(prenomDisplay);
    }

    public void setAgeDisplay(String ageDisplay) {
        this.ageDisplay.setText(ageDisplay);
    }



}
