package application.utils;

import java.sql.*;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/GestionEvenements";  // Remplace par le nom de ta base de données
        String user = "root";  // Remplace par ton utilisateur MySQL
        String password = "";  // Remplace par ton mot de passe MySQL

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Essayer de se connecter à la base de données
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("✅ Connexion réussie !");

                // Exécuter une requête pour récupérer les noms des utilisateurs
                String query = "SELECT nom FROM utilisateurs";  // Remplace 'utilisateurs' par le nom de ta table si nécessaire
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // Afficher les noms des utilisateurs
                while (rs.next()) {
                    String nom = rs.getString("nom");  // Récupérer le nom de chaque utilisateur
                    System.out.println("Nom de l'utilisateur : " + nom);
                }

                // Fermer la connexion après utilisation
                rs.close();
                stmt.close();
                conn.close();  // Fermer la connexion à la base de données
            } else {
                System.out.println("❌ Échec de la connexion !");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL !");
            e.printStackTrace();
        }
    }
}
