package org.example.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserList.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Gestion Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDetails.fxml"));

        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Personnes");
            primaryStage.show();

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
*/