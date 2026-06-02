package application.models;

import java.sql.Date;
import java.util.Objects;

public class utilisateurs {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private int roleId;
    private int nbEvenements;
    private Date dateInscription;
    private Date dateNaissance;
    private String evenementPrefere;

    // ✅ Constructeur vide (utile pour les setters ou JavaFX)
    public utilisateurs() {}

    // Constructeur principal
    public utilisateurs(int id, String nom, String email, String motDePasse, int roleId, Date dateInscription, Date dateNaissance, String evenementPrefere) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.roleId = roleId;
        this.dateInscription = dateInscription;
        this.dateNaissance = dateNaissance;
        this.evenementPrefere = evenementPrefere;
    }
    

    // ✅ Constructeur sans id (car auto-incrémenté)
    public utilisateurs(String nom, String email, String motDePasse, int roleId,
            int nbEvenements, Date dateInscription, Date dateNaissance, String evenementPrefere) {
this.nom = nom;
this.email = email;
this.motDePasse = motDePasse;
this.roleId = roleId;
this.nbEvenements = nbEvenements;
this.dateInscription = dateInscription;
this.dateNaissance = dateNaissance;
this.evenementPrefere = evenementPrefere;
}



    // Constructeur pour le nombre d'événements
    public utilisateurs(int id, String nom, String email, int nbEvenements, Date dateInscription, Date dateNaissance, String evenementPrefere) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.nbEvenements = nbEvenements;
        this.dateInscription = dateInscription;
        this.dateNaissance = dateNaissance;
        this.evenementPrefere = evenementPrefere;
    }

    // Constructeur sans id (utile lors de l'inscription)
    public utilisateurs(String nom, String email, String motDePasse, int roleId, Date dateInscription) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.roleId = roleId;
        this.dateInscription = dateInscription;
    }
    
    

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public int getNbEvenements() { return nbEvenements; }
    public void setNbEvenements(int nbEvenements) { this.nbEvenements = nbEvenements; }

    public Date getDateInscription() { return dateInscription; }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getEvenementPrefere() { return evenementPrefere; }
    public void setEvenementPrefere(String evenementPrefere) { this.evenementPrefere = evenementPrefere; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        utilisateurs that = (utilisateurs) o;
        return id == that.id && roleId == that.roleId && nbEvenements == that.nbEvenements && Objects.equals(nom, that.nom) && Objects.equals(email, that.email) && Objects.equals(motDePasse, that.motDePasse) && Objects.equals(dateInscription, that.dateInscription) && Objects.equals(dateNaissance, that.dateNaissance) && Objects.equals(evenementPrefere, that.evenementPrefere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, email, motDePasse, roleId, nbEvenements, dateInscription, dateNaissance, evenementPrefere);
    }
    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", nom=" + nom + ", email=" + email + 
               ", dateInscription=" + dateInscription + ", dateNaissance=" + dateNaissance + 
               ", evenementPrefere=" + evenementPrefere + ", nbEvenements=" + nbEvenements + "]";
    }
}
