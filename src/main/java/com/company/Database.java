package com.company;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * Created by Bogust on 10-12-2016.
 */
public class Database {

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Postgresql driver not found");
        }

        Connection connection = null;
        try {
            String URL = "jdbc:postgresql://localhost:5432/adv";
            String USERNAME = "postgres";
            String PASSWORD = "0";

            System.out.println("Connecting to database..");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Succeful connected to database");
        } catch (Exception e) {

        }

        return connection;
    }
}
