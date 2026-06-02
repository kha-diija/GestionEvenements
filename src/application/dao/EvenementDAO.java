package application.dao;

import application.models.Evenements;
import application.models.utilisateurs;
import application.utils.EmailService;
import application.utils.EmailUtil;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {
    private Connection conn;

    public EvenementDAO(Connection conn) {
        this.conn = conn;
    }
    public int getNombreInscriptionsActivesPourEvenement(int evenementId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Inscriptions WHERE evenement_id = ? AND statut = 'active'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, evenementId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Méthode pour supprimer un événement
    public void supprimerEvenement(int evenementId) {
        String deleteEvenementQuery = "DELETE FROM Evenements WHERE id = ?";
        String deleteInscriptionsQuery = "DELETE FROM Inscriptions WHERE evenement_id = ?";

        try {
            // Supprimer d'abord les inscriptions liées à l'événement
            try (PreparedStatement stmt = conn.prepareStatement(deleteInscriptionsQuery)) {
                stmt.setInt(1, evenementId);
                stmt.executeUpdate();
            }

            // Puis supprimer l'événement
            try (PreparedStatement stmt = conn.prepareStatement(deleteEvenementQuery)) {
                stmt.setInt(1, evenementId);
                stmt.executeUpdate();
            }

            System.out.println("Événement supprimé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterEvenement(String nom, String description, java.time.LocalDate date, String lieu, int capacite) {
        String query = "INSERT INTO Evenements (nom, description, date, lieu, capacite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, description);
            stmt.setDate(3, Date.valueOf(date));  // Convertir LocalDate en SQL Date
            stmt.setString(4, lieu);
            stmt.setInt(5, capacite);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public List<Evenements> getEvenementsDisponibles() {
        List<Evenements> evenementsDisponibles = new ArrayList<>();
        String query = "SELECT * FROM Evenements WHERE Capacite > (SELECT COUNT(*) FROM Inscriptions WHERE evenement_id = Evenements.id AND statut = 'active')";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Evenements evenement = new Evenements(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        rs.getString("lieu"),
                        rs.getInt("capacite"),
                        rs.getDouble("prix")
                );
                evenementsDisponibles.add(evenement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenementsDisponibles;
    }

    
 // Exemple de méthode dans EvenementDAO
    public boolean evenementExiste(String nom) {
        String query = "SELECT COUNT(*) FROM Evenements WHERE nom = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Si le nombre d'événements est supérieur à 0, cela signifie qu'un événement existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    
    public boolean annulerEvenement(int evenementId) {
        boolean succes = false;
        try {
            // Récupération des utilisateurs inscrits à cet événement
            InscriptionDAO inscriptionDAO = new InscriptionDAO(conn);
            List<utilisateurs> inscrits = inscriptionDAO.getInscriptionsParEvenement(evenementId);

            // Suppression de l'événement
            String sql = "DELETE FROM Evenements WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, evenementId);
                int affectedRows = stmt.executeUpdate();
                succes = affectedRows > 0;
            }

            // Envoi des emails si suppression réussie
            if (succes) {
                for (utilisateurs u : inscrits) {
                    String subject = "Annulation de l'événement";
                    String body = "Bonjour " + u.getNom() + ",\n\nL'événement auquel vous étiez inscrit a été annulé.\n\nCordialement,\nL'équipe d'organisation.";
                    EmailService.envoyerEmail(u.getEmail(), subject, body);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;
    }


    public List<Evenements> getAllEvenements() {
        List<Evenements> evenements = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Evenements";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                evenements.add(new Evenements(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getTimestamp("date"),
                    rs.getString("lieu"),
                    rs.getInt("capacite"),
                    rs.getDouble("prix") // ✅ Ajout du champ prix
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }
    
   
    public boolean ajouterConfirmation(int utilisateurId, int evenementId) {
        try {
            String getInscriptionIdQuery = "SELECT id FROM Inscriptions WHERE utilisateur_id = ? AND evenement_id = ? AND statut = 'active'";
            PreparedStatement ps = conn.prepareStatement(getInscriptionIdQuery);
            ps.setInt(1, utilisateurId);
            ps.setInt(2, evenementId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int inscriptionId = rs.getInt("id");

                String insertQuery = "INSERT INTO Confirmations (inscription_id, date_envoi) VALUES (?, NOW())";
                PreparedStatement insertPs = conn.prepareStatement(insertQuery);
                insertPs.setInt(1, inscriptionId);
                boolean inserted = insertPs.executeUpdate() > 0;

                if (inserted) {
                    String infoQuery = "SELECT u.email, u.nom, e.nom AS nom_evenement " +
                            "FROM Utilisateurs u " +
                            "JOIN Evenements e ON e.id = ? " +
                            "WHERE u.id = ?";
                    PreparedStatement infoPs = conn.prepareStatement(infoQuery);
                    infoPs.setInt(1, evenementId);
                    infoPs.setInt(2, utilisateurId);
                    ResultSet infoRs = infoPs.executeQuery();

                    if (infoRs.next()) {
                        String email = infoRs.getString("email");
                        String nomUser = infoRs.getString("nom");
                        String nomEvenement = infoRs.getString("nom_evenement");

                        // Envoi de l'email
                        EmailUtil.sendConfirmationEmail(email, nomUser, nomEvenement);
                    }
                }
                return inserted;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }




    
    public boolean ajouterInscription(int utilisateurId, int evenementId) {
        if (estDejaInscrit(utilisateurId, evenementId)) {
            return false;
        }

        String sql = "INSERT INTO Inscriptions (utilisateur_id, evenement_id, date_inscription, statut) VALUES (?, ?, NOW(), 'active')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public void ajouterEvenementReserveParUtilisateur(int idUtilisateur, int idEvenement) {
       String query = "INSERT INTO Inscriptions (utilisateur_id, evenement_id, date_inscription, statut) VALUES (?, ?, NOW(), 'active')";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idUtilisateur);
            statement.setInt(2, idEvenement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNombreInscritsPourEvenement(int evenementId) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM Inscriptions WHERE evenement_id = ? AND statut = 'active'";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, evenementId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // Nombre d'inscrits actifs pour cet événement
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur dans la console
        }

        return count;
    }

    
    public boolean estDejaInscrit(int utilisateurId, int evenementId) {
        String sql = "SELECT COUNT(*) FROM Inscriptions WHERE utilisateur_id = ? AND evenement_id = ? AND statut = 'active'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, evenementId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}
