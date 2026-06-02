package application.models;

public class InscriptionDetails {
    private int id;
    private String nomUtilisateur;
    private String nomEvenement;


    public InscriptionDetails(int id, String nomUtilisateur, String nomEvenement) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.nomEvenement = nomEvenement;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }
    
}
