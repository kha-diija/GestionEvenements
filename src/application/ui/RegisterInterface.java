package application.ui;

import application.dao.UtilisateurDAO;

import application.models.utilisateurs;
import application.utils.DatabaseConnection;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Date;

public class RegisterInterface {

    private StackPane root;

    public RegisterInterface() {
        buildUI();
    }

    private void buildUI() {
        root = new StackPane();

        ImageView background = new ImageView(new Image("file:/C:/Users/dell/Documents/bgevent.png"));
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        form.setMinWidth(500);
        form.setMaxWidth(550);
        form.setMinHeight(600);
        form.setMaxHeight(650);
        form.setStyle(
            "-fx-background-color: rgba(0,0,0,0.8);" +
            "-fx-background-radius: 25;" +
            "-fx-border-color: aqua;" +
            "-fx-border-radius: 25;" +
            "-fx-border-width: 2;" +
            "-fx-padding: 40;"
        );

        Label title = new Label("Créer un compte");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        title.setTextFill(Color.AQUA);
        title.setStyle("-fx-border-color: aqua; -fx-border-radius: 8; -fx-padding: 8;");

        TextField nameField = createStyledTextField("Nom");
        TextField emailField = createStyledTextField("Email");
        PasswordField passwordField = createStyledPasswordField("Mot de passe");

        DatePicker dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setPromptText("Date de naissance");
        styleInputField(dateNaissancePicker);

        TextField evenementPrefereField = createStyledTextField("Événement préféré");

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font(13));

        Button registerBtn = new Button("S'inscrire");
        registerBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: aqua;" +
            "-fx-text-fill: aqua;" +
            "-fx-border-radius: 20;" +
            "-fx-font-size: 16;" +
            "-fx-padding: 10 30;"
        );

        registerBtn.setOnAction(e -> {
            String nom = nameField.getText();
            String email = emailField.getText();
            String motDePasse = passwordField.getText();
            String dateNaissance = dateNaissancePicker.getValue() != null ? dateNaissancePicker.getValue().toString() : "";
            String evenementPrefere = evenementPrefereField.getText();

            if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || dateNaissance.isEmpty() || evenementPrefere.isEmpty()) {
                errorLabel.setText("❌ Veuillez remplir tous les champs.");
                return;
            }

            // Vérification de l'âge >= 6 ans
            java.time.LocalDate birthDate = dateNaissancePicker.getValue();
            java.time.LocalDate today = java.time.LocalDate.now();
            long age = java.time.temporal.ChronoUnit.YEARS.between(birthDate, today);
            if (age < 6) {
                errorLabel.setText("❌ Vous devez avoir au moins 6 ans pour vous inscrire.");
                return;
            }

            // Vérification du format de l'email
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                errorLabel.setText("❌ L'adresse email doit être une adresse @gmail.com.");
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                UtilisateurDAO dao = new UtilisateurDAO(conn);
                utilisateurs user = new utilisateurs();
                user.setNom(nom);
                user.setEmail(email);
                user.setMotDePasse(motDePasse);
                user.setRoleId(2);
                user.setDateInscription(new java.sql.Date(System.currentTimeMillis()));
                user.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
                user.setEvenementPrefere(evenementPrefere);

                try {
                    boolean success = dao.addUtilisateur(user);
                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inscription");
                        alert.setHeaderText(null);
                        alert.setContentText("✅ Compte créé avec succès !");
                        alert.showAndWait();

                        Stage stage = (Stage) root.getScene().getWindow();
                        stage.close();
                        new LoginInterfaceLauncher().start(new Stage());
                    } else {
                        errorLabel.setText("❌ Erreur lors de la création du compte.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorLabel.setText("❌ Erreur interne !");
                }
            } else {
                errorLabel.setText("❌ Connexion à la base échouée.");
            }
        });


        Hyperlink loginLink = new Hyperlink("Déjà un compte ? Connectez-vous");
        loginLink.setTextFill(Color.AQUA);
        loginLink.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        loginLink.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            new LoginInterfaceLauncher().start(new Stage());
        });

        form.getChildren().addAll(
            title,
            nameField,
            emailField,
            passwordField,
            dateNaissancePicker,
            evenementPrefereField,
            registerBtn,
            errorLabel,
            loginLink
        );

        root.getChildren().addAll(background, form);
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleInputField(field);
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        styleInputField(field);
        return field;
    }

    private void styleInputField(Control field) {
        field.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: aqua;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.6);" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 8;" +
            "-fx-font-size: 14;"
        );
        field.setMaxWidth(400);
    }

    public StackPane getView() {
        return root;
    }
}
