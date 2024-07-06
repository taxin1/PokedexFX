package com.example.Lab08_1B_210041106;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connection {
    private Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "pokedex";
        String databaseUser = "root";
        String databasePassword = "123456";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            //// Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle any exceptions here
        }

        return databaseLink;
    }
}
