package application.dao;

import application.models.utilisateurs;

import application.models.InscriptionDetails;
import application.models.Evenements;
import application.models.EvenementReserve;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InscriptionDAO {
    private final Connection conn;

    // Constructeur
    public InscriptionDAO(Connection conn) {
        this.conn = conn;
    }

    // ➕ Ajouter une inscription si pas déjà inscrite
    public boolean ajouterInscription(int utilisateurId, int evenementId) {
        if (estDejaInscrit(utilisateurId, evenementId)) {
            return false; // Déjà inscrit
        }

        String sql = "INSERT INTO Inscriptions (utilisateur_id, evenement_id, date_inscription, statut) " +
                     "VALUES (?, ?, NOW(), 'active')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getEmailsDesInscritsParEvenement(int evenementId) {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT u.email FROM inscriptions i JOIN utilisateurs u ON i.utilisateur_id = u.id WHERE i.evenement_id = ? AND i.statut = 'active'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, evenementId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    
    public boolean supprimerInscription(int utilisateurId, int evenementId) {
        String sql = "DELETE FROM Inscriptions WHERE utilisateur_id = ? AND evenement_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    

    // ✅ Vérifier si l'utilisateur est déjà inscrit à un événement actif
    public boolean estDejaInscrit(int utilisateurId, int evenementId) {
        String query = "SELECT COUNT(*) FROM Inscriptions WHERE utilisateur_id = ? AND evenement_id = ? AND statut = 'active'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<utilisateurs> getInscriptionsParEvenement(int evenementId) {
        List<utilisateurs> inscrits = new ArrayList<>();
        String sql = """
            SELECT u.id, u.nom, u.email, u.mot_de_passe, u.role_id, u.date_inscription, u.nb_evenements, u.date_naissance, u.evenement_prefere
            FROM Inscriptions i
            JOIN Utilisateurs u ON i.utilisateur_id = u.id
            WHERE i.evenement_id = ? AND i.statut = 'active'
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, evenementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	utilisateurs user = new utilisateurs(
                		    rs.getInt("id"),
                		    rs.getString("nom"),
                		    rs.getString("email"),
                		    rs.getString("mot_de_passe"),
                		    rs.getInt("role_id"),
                		    rs.getDate("date_inscription"),
                		    rs.getDate("date_naissance"),
                		    rs.getString("evenement_prefere")
                		);

                    inscrits.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscrits;
    }
    
    public List<EvenementReserve> getEvenementsReservesParUtilisateur(int utilisateurId) {
        List<EvenementReserve> evenements = new ArrayList<>();
        String sql = "SELECT e.id, e.nom, e.date, e.lieu, e.description " +
                     "FROM Evenements e " +
                     "JOIN Inscriptions i ON e.id = i.evenement_id " +
                     "WHERE i.utilisateur_id = ? AND i.statut = 'active'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EvenementReserve ev = new EvenementReserve(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("date"),
                    rs.getString("lieu"),
                    rs.getString("description")
                );
                evenements.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return evenements;
    }




    // 📋 Liste des inscriptions avec nom utilisateur et événement
    public List<InscriptionDetails> getAllInscriptionsAvecDetails() {
        List<InscriptionDetails> inscriptionsList = new ArrayList<>();
        String query = """
            SELECT i.id, u.nom AS nom_utilisateur, e.nom AS nom_evenement
            FROM Inscriptions i
            JOIN utilisateurs u ON i.utilisateur_id = u.id
            JOIN evenements e ON i.evenement_id = e.id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomUtilisateur = rs.getString("nom_utilisateur");
                String nomEvenement = rs.getString("nom_evenement");

                inscriptionsList.add(new InscriptionDetails(id, nomUtilisateur, nomEvenement));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscriptionsList;
    }
    
    public int getNombreEvenementsActifsParUtilisateur(int utilisateurId) {
        String query = "SELECT COUNT(*) FROM Inscriptions WHERE utilisateur_id = ? AND statut = 'active'";
        
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, utilisateurId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    

    // 🔢 Récupère tableau [événement, utilisateur] pour une table simple
    public List<String[]> getInscriptionsAvecNoms() {
        List<String[]> inscriptions = new ArrayList<>();
        String sql = """
            SELECT u.nom AS utilisateur_nom, e.nom AS evenement_nom
            FROM Inscriptions i
            JOIN Utilisateurs u ON i.utilisateur_id = u.id
            JOIN Evenements e ON i.evenement_id = e.id
            ORDER BY e.nom ASC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String utilisateurNom = rs.getString("utilisateur_nom");
                String evenementNom = rs.getString("evenement_nom");
                inscriptions.add(new String[]{evenementNom, utilisateurNom});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscriptions;
    }

    // 📅 Inscriptions actives d’un utilisateur
    public List<Map<String, String>> getInscriptionsActivesParUtilisateur(int utilisateurId) {
        List<Map<String, String>> inscriptions = new ArrayList<>();
        String sql = """
            SELECT e.nom AS evenement, i.date_inscription
            FROM Inscriptions i
            JOIN Evenements e ON i.evenement_id = e.id
            WHERE i.utilisateur_id = ? AND i.statut = 'active'
            ORDER BY i.date_inscription DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("evenement", rs.getString("evenement"));
                    map.put("date_inscription", rs.getString("date_inscription"));
                    inscriptions.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscriptions;
    }
}
