package com.vidora;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/vidora";
            String user = "root";
            String password = "eloge16king2";

            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }
}