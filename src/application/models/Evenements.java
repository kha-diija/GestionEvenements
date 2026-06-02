package application.models;

import java.util.Date;

public class Evenements {
    private int id;
    private String nom;
    private String description;
    private Date date;
    private String lieu;
    private int capacite;
    private double prix; // ✅ nouveau champ

    // Constructeur
    public Evenements(int id, String nom, String description, Date date, String lieu, int capacite, double prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.capacite = capacite;
        this.prix = prix;
    }

    
    
    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
}
