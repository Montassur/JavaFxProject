package org.example.tests;
import org.example.entities.User;
import org.example.services.UserServices;

import java.sql.SQLException;
import java.util.ArrayList;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        UserServices ps = new UserServices();
        try {
            //ps.ajouter(new Personne("Lahbaieb", "Ranim", 24));
            //ps.supprimer(2);
            //User p1 = new User(3 ,"Lahbaieb" , "Ranim",24);
           // ps.modifier(p1);
            System.out.println(ps.getAll());
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }



    }
}