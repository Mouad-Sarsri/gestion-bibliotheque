package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mouad
 **/
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque?serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    // Singleton : une seule connexion reutilisee


    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

            } catch(ClassNotFoundException e) {
                throw new SQLException("Driver JDBC MySQL introuvable. "
                + "Vérifiez que mysql-connector-j est bien dans le classpath.", e);
            }
        }
        return connection;
    }
}
