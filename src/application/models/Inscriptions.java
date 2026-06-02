package application.models;

import java.util.Date;

public class Inscriptions {
    private int id;
    private int utilisateurId;
    private int evenementId;
    private Date dateInscription;
    private String statut;
    private utilisateurs utilisateur;

    // Constructeur
    public Inscriptions(int id, int utilisateurId, int evenementId, Date dateInscription, String statut) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.evenementId = evenementId;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }

    public int getEvenementId() { return evenementId; }
    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }

    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

}
