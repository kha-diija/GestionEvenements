package application.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/GestionEvenements"; // Change database name if needed
    private static final String USER = "root"; // Change if you have another MySQL username
    private static final String PASSWORD = ""; // Change if you have a password

    private static Connection conn;

    // Méthode pour obtenir la connexion
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Établir la connexion
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base de données réussie !");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver JDBC non trouvé !");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Erreur de connexion à la base de données !");
                e.printStackTrace();
            }
        }
        return conn;}}
    
