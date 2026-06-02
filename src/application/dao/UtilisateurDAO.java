package application.dao;

import application.models.utilisateurs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private final Connection conn;

    public UtilisateurDAO(Connection conn) {
        this.conn = conn;
    }

    public List<utilisateurs> getAllUtilisateurs() {
        List<utilisateurs> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, mot_de_passe, role_id, date_inscription, date_naissance, evenement_prefere FROM utilisateurs";

        System.out.println("Requête SQL : " + sql);  // Ajouter cette ligne pour vérifier la requête SQL
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

        	while (rs.next()) {
        	    utilisateurs u = new utilisateurs(
        	        rs.getInt("id"),
        	        rs.getString("nom"),
        	        rs.getString("email"),
        	        rs.getString("mot_de_passe"),
        	        rs.getInt("role_id"),
        	        rs.getDate("date_inscription") != null ? rs.getDate("date_inscription") : new java.sql.Date(0),  // Valeur par défaut si null
        	        rs.getDate("date_naissance") != null ? rs.getDate("date_naissance") : new java.sql.Date(0),  // Valeur par défaut si null
        	        rs.getString("evenement_prefere")
        	    );
        	    liste.add(u);
        	}

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }

        return liste;
    }


    public utilisateurs getUtilisateurById(int id) {
        utilisateurs utilisateur = null;
        String query = "SELECT * FROM Utilisateurs WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                utilisateur = new utilisateurs(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("role_id"),
                        rs.getDate("date_inscription"),
                        rs.getDate("date_naissance"),
                        rs.getString("evenement_prefere")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    
    
    public boolean ajouterUtilisateur(utilisateurs utilisateur) {
        String sql = "INSERT INTO Utilisateurs (nom, email, mot_de_passe, role_id, date_inscription, date_naissance, evenement_prefere) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setInt(4, utilisateur.getRoleId());

            java.sql.Date dateInscription = new java.sql.Date(utilisateur.getDateInscription().getTime());
            stmt.setDate(5, dateInscription);

            if (utilisateur.getDateNaissance() != null) {
                java.sql.Date dateNaissance = new java.sql.Date(utilisateur.getDateNaissance().getTime());
                stmt.setDate(6, dateNaissance);
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setString(7, utilisateur.getEvenementPrefere());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // ✅ Obtenir l'ID d'un rôle par nom
    public int getRoleIdByName(String roleName) throws SQLException {
        String query = "SELECT id FROM Roles WHERE nom_role = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // Retourne -1 si le rôle n'existe pas
            }
        }
    }

    public List<utilisateurs> getUtilisateursParEvenement(int evenementId) {
        List<utilisateurs> liste = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.email, u.mot_de_passe, u.role_id, u.date_inscription, u.date_naissance, u.evenement_prefere "
                     + "FROM utilisateurs u "
                     + "JOIN Inscriptions i ON u.id = i.utilisateur_id "
                     + "WHERE i.evenement_id = ? AND i.statut = 'active'";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, evenementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs u = new utilisateurs(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("role_id"),
                        rs.getDate("date_inscription") != null ? rs.getDate("date_inscription") : new java.sql.Date(0),
                        rs.getDate("date_naissance") != null ? rs.getDate("date_naissance") : new java.sql.Date(0),
                        rs.getString("evenement_prefere")
                    );
                    liste.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    public boolean emailExiste(String email) {
        String query = "SELECT COUNT(*) FROM Utilisateurs WHERE email = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    // ✅ Ajouter un utilisateur
    public boolean addUtilisateur(utilisateurs user) {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, role_id, date_inscription, date_naissance, evenement_prefere) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getMotDePasse());
            ps.setInt(4, user.getRoleId());
            ps.setDate(5, new java.sql.Date(user.getDateInscription().getTime()));
            ps.setDate(6, new java.sql.Date(user.getDateNaissance().getTime())); // Nouveau champ
            ps.setString(7, user.getEvenementPrefere()); // Nouveau champ

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur dans la console
            return false;        // Retourne false si l'insertion échoue
        }
    }

    // Autres méthodes comme verifierConnexion, updatePasswordByEmail restent les mêmes.


    public utilisateurs verifierConnexion(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new utilisateurs(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe"),
                            rs.getInt("role_id"),
                            rs.getDate("date_inscription"),
                            rs.getDate("date_naissance"), // Nouveau champ
                            rs.getString("evenement_prefere") // Nouveau champ
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean supprimerUtilisateurParId(int id) {
        String query = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUtilisateur(utilisateurs utilisateur) {
        String query = "UPDATE Utilisateurs SET nom = ?, email = ?, mot_de_passe = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setInt(4, utilisateur.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public List<utilisateurs> getUtilisateursWithReservationCount() {
        List<utilisateurs> utilisateursList = new ArrayList<>();
        String query = """
            SELECT u.id, u.nom, u.email, COUNT(i.id) AS nb_evenements, u.date_inscription, u.date_naissance, u.evenement_prefere
            FROM utilisateurs u
            LEFT JOIN inscriptions i ON u.id = i.utilisateur_id AND i.statut = 'active'
            GROUP BY u.id
        """;
        
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String email = resultSet.getString("email");
                int nbEvenements = resultSet.getInt("nb_evenements");
                Date dateInscription = resultSet.getDate("date_inscription");
                Date dateNaissance = resultSet.getDate("date_naissance");
                String evenementPrefere = resultSet.getString("evenement_prefere");

                // Créer un objet utilisateurs avec le constructeur adapté
                utilisateurs utilisateur = new utilisateurs(id, nom, email, nbEvenements, dateInscription, dateNaissance, evenementPrefere);
                utilisateursList.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return utilisateursList;
    }
    
    
    
    public int getNombreEvenementsReserves(int utilisateurId) {
        String query = "SELECT COUNT(*) FROM Inscriptions WHERE utilisateur_id = ? AND statut = 'active'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);  // Retourne le nombre d'événements réservés
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // Retourne 0 si aucune inscription active n'est trouvée
    }


    
}
