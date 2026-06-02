package application.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StatistiquesDAO {
    private final Connection conn;

    public StatistiquesDAO(Connection conn) {
        this.conn = conn;
    }

    public int getNombreUtilisateurs() {
        return count("SELECT COUNT(*) FROM Utilisateurs");
    }

    public int getNombreEvenements() {
        return count("SELECT COUNT(*) FROM Evenements");
    }

    public int getNombreUtilisateursInscrits() {
        return count("SELECT COUNT(DISTINCT utilisateur_id) FROM Inscriptions WHERE statut = 'active'");
    }

    public int getNombreTotalInscriptions() {
        return count("SELECT COUNT(*) FROM Inscriptions WHERE statut = 'active'");
    }

    public int getCapaciteTotaleEvenements() {
        return count("SELECT SUM(capacite) FROM Evenements");
    }

    public int getNombreEvenementsComplets() {
        return count(
            "SELECT COUNT(*) FROM Evenements e " +
            "WHERE (SELECT COUNT(*) FROM Inscriptions i WHERE i.evenement_id = e.id AND i.statut = 'active') >= e.capacite"
        );
    }

    public int getNombreEvenementsReserves() {
        return count(
            "SELECT COUNT(DISTINCT evenement_id) FROM Inscriptions WHERE statut = 'active'"
        );
    }

    private int count(String query) {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
