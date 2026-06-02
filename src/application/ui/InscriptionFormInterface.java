package application.ui;
import application.dao.EvenementDAO;
import application.dao.InscriptionDAO;
import application.dao.UtilisateurDAO;
import application.models.Evenements;
import application.models.Inscriptions;
import application.models.utilisateurs;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import application.utils.EmailUtil;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class InscriptionFormInterface {

    private final InscriptionDAO inscriptionDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final EvenementDAO evenementDAO;

    public InscriptionFormInterface(Connection conn) {
        this.inscriptionDAO = new InscriptionDAO(conn);
        this.utilisateurDAO = new UtilisateurDAO(conn);
        this.evenementDAO = new EvenementDAO(conn);
    }

    public VBox getView() {
        VBox form = new VBox(20);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        form.setStyle("-fx-background-color: #000000;");

        Text title = new Text("Formulaire d'inscription à un événement");
        title.setFont(Font.font("Verdana", 24));  // Changer pour une police moderne
        title.setStyle("-fx-fill: #00FFFF; -fx-font-weight: bold; -fx-padding: 10;");


        ComboBox<utilisateurs> utilisateurComboBox = new ComboBox<>();
        ComboBox<Evenements> evenementComboBox = new ComboBox<>();

        // Chargement des utilisateurs
     // Chargement des utilisateurs
        List<utilisateurs> utilisateursList = utilisateurDAO.getAllUtilisateurs();
        utilisateurComboBox.setItems(FXCollections.observableArrayList(utilisateursList));
        utilisateurComboBox.setPromptText("Sélectionner un utilisateur");

        // Appliquer un style de couleur de fond différent à la ComboBox
        utilisateurComboBox.setStyle("-fx-background-color: #444444; -fx-border-color: #00B2B2; -fx-border-radius: 5px; -fx-font-family: 'Verdana'; -fx-font-size: 14px; -fx-text-fill: white;");

        // Personnalisation des cellules de la ComboBox pour afficher les noms dans la liste déroulante
        utilisateurComboBox.setCellFactory(param -> new ListCell<utilisateurs>() {
            @Override
            protected void updateItem(utilisateurs item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom());  // Affichage du nom de l'utilisateur dans la liste
                    setStyle("-fx-background-color: white; -fx-text-fill: #333333;");  // Fond noir et texte blanc dans la liste déroulante
                }
            }
        });

        // Personnalisation de la cellule du bouton de la ComboBox (zone de sélection actuelle)
        // Cette cellule contrôle ce qui s'affiche une fois un élément sélectionné
        utilisateurComboBox.setButtonCell(new ListCell<utilisateurs>() {
            @Override
            protected void updateItem(utilisateurs item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Sélectionner un utilisateur");
                } else {
                    setText(item.getNom());  // Affichage du nom sélectionné dans la barre
                }
                // Application du style pour le texte sélectionné en blanc sur fond foncé
                setStyle("-fx-background-color: #444444; -fx-text-fill: white;");  // Texte en blanc, fond foncé pour la sélection
            }
        });




     // Chargement des événements
     // Chargement des événements
        List<Evenements> evenementsList = evenementDAO.getAllEvenements();
        evenementComboBox.setItems(FXCollections.observableArrayList(evenementsList));
        evenementComboBox.setPromptText("Sélectionner un événement");

        // Appliquer un style de couleur de fond différent à la ComboBox
        evenementComboBox.setStyle("-fx-background-color: #444444; -fx-border-color: #00B2B2; -fx-border-radius: 8px; -fx-font-family: 'Verdana'; -fx-font-size: 14px; -fx-text-fill: white;");

        // Personnalisation des cellules de la ComboBox pour afficher les noms dans la liste déroulante
        evenementComboBox.setCellFactory(param -> new ListCell<Evenements>() {
            @Override
            protected void updateItem(Evenements item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom());  // Affichage du nom de l'événement dans la liste
                    setStyle("-fx-background-color: white; -fx-text-fill: #333333;");  // Fond noir et texte blanc dans la liste déroulante
                }
            }
        });

        // Personnalisation de la cellule du bouton de la ComboBox (zone de sélection actuelle)
        // Cette cellule contrôle ce qui s'affiche une fois un élément sélectionné
        evenementComboBox.setButtonCell(new ListCell<Evenements>() {
            @Override
            protected void updateItem(Evenements item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Sélectionner un événement");
                } else {
                    setText(item.getNom());  // Affichage du nom sélectionné dans la barre
                }
                // Application du style pour le texte sélectionné en blanc sur fond foncé
                setStyle("-fx-background-color: #444444; -fx-text-fill: white;");  // Texte en blanc, fond foncé pour la sélection
            }
        });



        Button inscrireBtn = new Button("S'inscrire");
        inscrireBtn.setStyle("-fx-background-color: #00B2B2; -fx-text-fill: white;");
     // Appliquer un style avec animation au bouton inscrireBtn
        inscrireBtn.setStyle("-fx-background-color: #00B2B2; "
                + "-fx-text-fill: white; "
                + "-fx-font-family: 'Verdana'; "
                + "-fx-font-size: 16px; "
                + "-fx-padding: 10px 20px; "  // Augmente la taille du bouton
                + "-fx-background-radius: 5px; "  // Arrondir les coins
                + "-fx-border-radius: 5px; ");

        // Ajouter une animation de survol (hover) pour agrandir et changer la couleur du bouton
        inscrireBtn.setOnMouseEntered(event -> {
            inscrireBtn.setStyle("-fx-background-color: #00A1A1; "
                    + "-fx-text-fill: white; "
                    + "-fx-font-family: 'Verdana'; "
                    + "-fx-font-size: 16px; "
                    + "-fx-padding: 12px 24px; "  // Augmenter la taille quand survolé
                    + "-fx-background-radius: 5px; "
                    + "-fx-border-radius: 5px;");
        });

        inscrireBtn.setOnMouseExited(event -> {
            inscrireBtn.setStyle("-fx-background-color: #00B2B2; "
                    + "-fx-text-fill: white; "
                    + "-fx-font-family: 'Verdana'; "
                    + "-fx-font-size: 16px; "
                    + "-fx-padding: 10px 20px; "  // Retour à la taille normale
                    + "-fx-background-radius: 5px; "
                    + "-fx-border-radius: 5px;");
        });

        inscrireBtn.setOnAction(e -> {
            utilisateurs selectedUser = utilisateurComboBox.getValue();
            Evenements selectedEvent = evenementComboBox.getValue();

            if (selectedUser == null || selectedEvent == null) {
                showAlert("Erreur", "Veuillez sélectionner un utilisateur et un événement.");
                return;
            }

            // Vérification si déjà inscrit
            boolean dejaInscrit = inscriptionDAO.estDejaInscrit(selectedUser.getId(), selectedEvent.getId());

            if (dejaInscrit) {
                showAlert("Erreur", "Cet utilisateur est déjà inscrit à cet événement.");
                return;
            }

            // Ajout de l'inscription
            Inscriptions inscription = new Inscriptions(0, selectedUser.getId(), selectedEvent.getId(), new Date(), "active");
            boolean success = inscriptionDAO.ajouterInscription(selectedUser.getId(), selectedEvent.getId());

            if (success) {
                showAlert("Succès", "Inscription enregistrée !");
                
                // Envoi email de confirmation
                String email = selectedUser.getEmail();
                String nomUser = selectedUser.getNom();
                String nomEvenement = selectedEvent.getNom();

                try {
                    // Appel à la méthode d'envoi d'email de confirmation
                    EmailUtil.sendConfirmationEmail(email, nomUser, nomEvenement);
                } catch (MessagingException ex) {
                    // Gestion de l'exception pour les erreurs d'envoi de message
                    ex.printStackTrace();
                    showAlert("Erreur d'envoi", "L'inscription a été enregistrée, mais l'email n'a pas pu être envoyé.");
                } catch (UnsupportedEncodingException ex) {
                    // Gestion de l'exception pour les erreurs d'encodage d'email
                    ex.printStackTrace();
                    showAlert("Erreur d'envoi", "L'inscription a été enregistrée, mais le format de l'email n'est pas supporté.");
                }
            } else {
                showAlert("Échec", "Échec de l'inscription.");
            }

        });

        form.getChildren().addAll(title, utilisateurComboBox, evenementComboBox, inscrireBtn);
        return form;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}