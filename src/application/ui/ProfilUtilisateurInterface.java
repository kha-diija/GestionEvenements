package application.ui;

import application.models.utilisateurs;
import application.dao.UtilisateurDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;

//... les imports restent inchangés

public class ProfilUtilisateurInterface {

 private final utilisateurs utilisateurConnecte;
 private final UtilisateurDAO utilisateurDAO;
 private VBox view;

 public ProfilUtilisateurInterface(utilisateurs utilisateur, Connection conn) {
     this.utilisateurConnecte = utilisateur;
     this.utilisateurDAO = new UtilisateurDAO(conn);
     buildUI();
 }

 public Node getView() {
     return view;
 }

 private void buildUI() {
     view = new VBox(30);
     view.setAlignment(Pos.TOP_CENTER);
     view.setStyle("-fx-background-color: #2B2B2B;");
     view.setPadding(new Insets(50));

     Label title = new Label("Mon Profil");
     title.setTextFill(Color.WHITE);
     VBox.setMargin(title, new Insets(0, 0, 30, 0));
     title.setFont(Font.font("Arial", 28));
     title.setTextFill(Color.web("#00DCDC"));

     VBox formContainer = new VBox(15);
     formContainer.setAlignment(Pos.CENTER_LEFT);
     formContainer.setPadding(new Insets(25));
     formContainer.setSpacing(15);
     formContainer.setMaxWidth(500);
     formContainer.setStyle(
             "-fx-background-color: #1F1F1F;" +
             "-fx-border-color: #00CCCC;" +
             "-fx-border-radius: 15;" +
             "-fx-background-radius: 15;" +
             "-fx-effect: dropshadow(gaussian, #00FFFFAA, 10, 0.5, 0, 0);"
     );

     // Nom
     Label nomLabel = createLabel("Nom :");
     TextField nomField = createTextField(utilisateurConnecte.getNom());

     // Email
     Label emailLabel = createLabel("Email :");
     TextField emailField = createTextField(utilisateurConnecte.getEmail());

     // Date de naissance
     Label dateNaissanceLabel = createLabel("Date de naissance :");
     DatePicker dateNaissancePicker = new DatePicker();
     if (utilisateurConnecte.getDateNaissance() != null)
         dateNaissancePicker.setValue(utilisateurConnecte.getDateNaissance().toLocalDate());

     // Événement préféré
     Label eventLabel = createLabel("Événement préféré :");
     TextArea eventField = new TextArea(utilisateurConnecte.getEvenementPrefere());
     eventField.setWrapText(true);
     eventField.setPrefRowCount(2);
     eventField.setPrefWidth(370);
     eventField.setMaxWidth(400);
     eventField.setMinHeight(70);
     eventField.setStyle("-fx-background-color: #0000; -fx-text-fill: #000000; -fx-border-color: transparent; -fx-padding: 8;");

     // Mot de passe
     Label motDePasseLabel = createLabel("Mot de passe :");
     PasswordField motDePasseField = createPasswordField();

     // Confirmation
     Label confirmLabel = createLabel("Confirmer le mot de passe :");
     PasswordField confirmMotDePasseField = createPasswordField();

     // Bouton de mise à jour
     Button saveButton = new Button("Mettre à jour");
     saveButton.setStyle("-fx-background-color: #00CCCC; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30 10 30;");
     saveButton.setOnAction(e -> {
         String nom = nomField.getText();
         String email = emailField.getText();
         String motDePasse = motDePasseField.getText();
         String confirm = confirmMotDePasseField.getText();

         // Vérif email
         if (!email.endsWith("@gmail.com")) {
             showAlert("Erreur", "L'adresse email doit se terminer par @gmail.com.");
             return;
         }

         // Vérif date de naissance (> 6 ans)
         if (dateNaissancePicker.getValue() == null) {
             showAlert("Erreur", "Veuillez sélectionner une date de naissance.");
             return;
         }
         java.time.LocalDate dateNaissance = dateNaissancePicker.getValue();
         java.time.LocalDate today = java.time.LocalDate.now();
         if (java.time.Period.between(dateNaissance, today).getYears() < 6) {
             showAlert("Erreur", "L'utilisateur doit avoir au moins 6 ans.");
             return;
         }

         // Vérif mot de passe
         if (!motDePasse.equals(confirm)) {
             showAlert("Erreur", "Les mots de passe ne correspondent pas.");
             return;
         }

         utilisateurConnecte.setNom(nom);
         utilisateurConnecte.setEmail(email);
         if (!motDePasse.isEmpty()) utilisateurConnecte.setMotDePasse(motDePasse);
         utilisateurConnecte.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
         utilisateurConnecte.setEvenementPrefere(eventField.getText());

         boolean success = utilisateurDAO.updateUtilisateur(utilisateurConnecte);
         if (success) showAlert("Succès", "Profil mis à jour avec succès.");
         else showAlert("Erreur", "Échec de la mise à jour.");
     });

     HBox buttonContainer = new HBox(saveButton);
     buttonContainer.setAlignment(Pos.CENTER);
     buttonContainer.setPadding(new Insets(20, 0, 0, 0));

     formContainer.getChildren().addAll(
             nomLabel, nomField,
             emailLabel, emailField,
             dateNaissanceLabel, dateNaissancePicker,
             eventLabel, eventField,
             motDePasseLabel, motDePasseField,
             confirmLabel, confirmMotDePasseField,
             buttonContainer
     );

     view.getChildren().addAll(title, formContainer);
 }

 private Label createLabel(String text) {
     Label label = new Label(text);
     label.setTextFill(Color.WHITE);
     label.setFont(Font.font("Arial", 15));
     return label;
 }

 private TextField createTextField(String text) {
     TextField field = new TextField(text);
     field.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: transparent;");
     field.setPrefWidth(350);
     return field;
 }

 private PasswordField createPasswordField() {
     PasswordField field = new PasswordField();
     field.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: transparent;");
     field.setPrefWidth(350);
     return field;
 }

 private void showAlert(String titre, String message) {
     Alert alert = new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle(titre);
     alert.setHeaderText(null);
     alert.setContentText(message);
     alert.showAndWait();
 }
}
