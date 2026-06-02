package application.models;

public class EvenementReserve {
    private int id;
    private String nom;
    private String date;
    private String lieu;
    private String description;

    // Nouveau constructeur avec ID
    public EvenementReserve(int id, String nom, String date, String lieu, String description) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.description = description;
    }

    // Ancien constructeur sans ID (pour compatibilité)
    public EvenementReserve(String nom, String date, String lieu, String description) {
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.description = description;
    }

    // Getter pour ID
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDate() {
        return date;
    }

    public String getLieu() {
        return lieu;
    }

    public String getDescription() {
        return description;
    }

    // Setters optionnels
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
