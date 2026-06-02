package application.ui;

import application.dao.UtilisateurDAO;
import application.models.utilisateurs;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.sql.Date;


public class AjoutUtilisateurInterface {
    private final UtilisateurDAO utilisateurDAO;
    private VBox view;

    public AjoutUtilisateurInterface(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
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

        Text title = new Text("Ajouter un utilisateur");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.web("#00DCDC"));
        VBox.setMargin(title, new Insets(0, 0, 30, 0));

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

        // Champs
        Label nomLabel = createLabel("Nom");
        TextField nomField = createTextField("Nom");

        Label emailLabel = createLabel("Email");
        TextField emailField = createTextField("Email");

        Label mdpLabel = createLabel("Mot de passe");
        PasswordField motDePasseField = createPasswordField("Mot de passe");

        Label mdpConfirmLabel = createLabel("Confirmer le mot de passe");
        PasswordField motDePasseConfirmField = createPasswordField("Confirmer le mot de passe");

     // Champ pour le rôle
        Label roleLabel = createLabel("Nom du rôle");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Utilisateur", "Administrateur");
        roleComboBox.setPrefWidth(350);
        roleComboBox.setValue("Utilisateur");
        roleComboBox.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        roleComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sélectionnez un rôle" : item);
                setTextFill(Color.WHITE);
                setStyle("-fx-background-color: #333;");
            }
        });


        Label eventPrefLabel = createLabel("Événement préféré");
        TextField evenementPrefereField = createTextField("Événement préféré");

        Label dateNaissanceLabel = createLabel("Date de naissance");
        DatePicker dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setPrefWidth(350);
        dateNaissancePicker.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Button enregistrerBtn = new Button("Enregistrer");
        enregistrerBtn.setStyle(
            "-fx-background-color: #00CCCC;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 30 10 30;" +
            "-fx-background-radius: 10;"
        );

        enregistrerBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText().trim();
                String email = emailField.getText().trim();
                String motDePasse = motDePasseField.getText();
                String motDePasseConfirm = motDePasseConfirmField.getText();
                String role = roleComboBox.getValue();
                String evenementPrefere = evenementPrefereField.getText().trim();
                LocalDate localDateNaissance = dateNaissancePicker.getValue();

                if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || motDePasseConfirm.isEmpty() || role == null || localDateNaissance == null) {
                    showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                    return;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    showAlert("Erreur", "Email invalide.");
                    return;
                }

                if (!motDePasse.equals(motDePasseConfirm)) {
                    showAlert("Erreur", "Les mots de passe ne correspondent pas.");
                    return;
                }

                if (utilisateurDAO.emailExiste(email)) {
                    showAlert("Erreur", "Un utilisateur avec cet email existe déjà.");
                    return;
                }

                int age = Period.between(localDateNaissance, LocalDate.now()).getYears();
                if (age < 6) {
                    showAlert("Erreur", "L'utilisateur doit avoir au moins 6 ans.");
                    return;
                }

                Date dateNaissance = Date.valueOf(localDateNaissance);
                Date dateInscription = new Date(System.currentTimeMillis());
                int roleId = role.equals("Administrateur") ? 1 : 2;

                utilisateurs user = new utilisateurs(
                    nom, email, motDePasse, roleId, 0,
                    dateInscription, dateNaissance, evenementPrefere
                );

                boolean success = utilisateurDAO.ajouterUtilisateur(user);
                if (success) {
                    showAlert("Succès", "Utilisateur ajouté avec succès !");
                    nomField.clear();
                    emailField.clear();
                    motDePasseField.clear();
                    motDePasseConfirmField.clear();
                    roleComboBox.setValue("Utilisateur");
                    evenementPrefereField.clear();
                    dateNaissancePicker.setValue(null);
                } else {
                    showAlert("Erreur", "Échec de l'ajout de l'utilisateur.");
                }

            } catch (DateTimeParseException ex) {
                showAlert("Erreur", "Date de naissance invalide.");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'ajout : " + ex.getMessage());
            }
        });

        HBox buttonContainer = new HBox(enregistrerBtn);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));

        formContainer.getChildren().addAll(
        	    nomLabel, nomField,
        	    emailLabel, emailField,
        	    mdpLabel, motDePasseField,
        	    mdpConfirmLabel, motDePasseConfirmField,
        	    roleLabel, roleComboBox, // 🆕 ligne ajoutée ici
        	    eventPrefLabel, evenementPrefereField,
        	    dateNaissanceLabel, dateNaissancePicker,
        	    buttonContainer
        	);


        view.getChildren().addAll(title, formContainer);
    }

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(350);
        field.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: transparent;");
        return field;
    }

    private PasswordField createPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setPrefWidth(350);
        field.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: transparent;");
        return field;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 14));
        return label;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}