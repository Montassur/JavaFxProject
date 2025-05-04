package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.LayoutController;
import org.example.services.MailService;
import org.example.services.AuthService;
import org.example.utils.MyDataBase;

public class MainApp extends Application {
    // Exposed so controllers can grab the same instance
    public static AuthService AUTH_SERVICE;
    public static LayoutController LAYOUT_CONTROLLER;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1) Configure your Gmail SMTP
        String gmailUser    = "montassar121@gmail.com";
        String gmailAppPass = "hjib ndkm itec wurh";
        MailService mailService = new MailService(gmailUser, gmailAppPass);

        // 2) Build your AuthService once
        AUTH_SERVICE = new AuthService(
                MyDataBase.getInstance().getConnection(),
                mailService
        );


        // 3) Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Connexion");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
