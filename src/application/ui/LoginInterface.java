package application.ui;

import application.dao.UtilisateurDAO;
import application.models.utilisateurs;
import application.utils.DatabaseConnection;
import application.utils.EmailUtil;
import application.utils.Session;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import java.sql.Connection;

public class LoginInterface {

    private StackPane root;
    private Runnable onLoginSuccess;

    public LoginInterface() {
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
        form.setMinWidth(420);
        form.setMaxWidth(450);
        form.setMinHeight(380);
        form.setMaxHeight(400);
        form.setStyle(
            "-fx-background-color: rgba(0,0,0,0.85);" +
            "-fx-background-radius: 25 0 0 25;" +
            "-fx-border-radius: 25 0 0 25;" +
            "-fx-border-width: 3;" +
            "-fx-border-insets: -1;" +
            "-fx-border-color: linear-gradient(to bottom right, cyan, magenta);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,255,255,0.6), 10, 0.3, 0, 4);" +
            "-fx-padding: 30;"
        );

        Label title = new Label("Login");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        title.setTextFill(Color.AQUA);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        styleInputField(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        styleInputField(passwordField);

        Button loginBtn = new Button("Se connecter");
        loginBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: aqua;" +
            "-fx-text-fill: aqua;" +
            "-fx-border-radius: 15;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 8 20;"
        );

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                UtilisateurDAO dao = new UtilisateurDAO(conn);
                utilisateurs user = dao.verifierConnexion(email, password);
                if (user != null) {
                    // Enregistrer l'utilisateur dans la session
                    Session.setCurrentUser(user);
                    
                    // Vérifier si l'utilisateur est bien connecté
                    if (Session.getCurrentUser() != null) {
                        System.out.println("Utilisateur connecté : " + Session.getCurrentUser().getEmail());
                    } else {
                        System.out.println("Erreur: Aucun utilisateur trouvé dans la session.");
                    }

                    // Ferme la fenêtre de connexion
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close();

                    // Ouvre l'interface appropriée en fonction du rôle de l'utilisateur
                    try {
                        if (user.getRoleId() == 1) {
                            new MainApp().start(new Stage()); // Ouvre l'interface administrateur
                        } else if (user.getRoleId() == 2) {
                            new UserMainApp().start(new Stage()); // Ouvre l'interface utilisateur standard
                        } else {
                            errorLabel.setText("Rôle inconnu !");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    errorLabel.setText("❌ Email ou mot de passe incorrect !");
                }
            } else {
                errorLabel.setText("❌ Erreur de connexion à la base !");
            }
        });

        // Hyperlink pour mot de passe oublié
        Hyperlink forgot = new Hyperlink("Mot de passe oublié ?");
        forgot.setTextFill(Color.AQUA);
        forgot.setOnAction(e -> {
            String email = emailField.getText();
            if (email == null || email.isEmpty()) {
                errorLabel.setText("❌ Entrez votre email !");
                return;
            }

            String resetCode = EmailUtil.generateResetCode();
            try {
                EmailUtil.sendResetCode(email, resetCode);
                System.out.println("Code envoyé à l'adresse : " + email);
            } catch (MessagingException | UnsupportedEncodingException ex) {
                ex.printStackTrace();
                errorLabel.setText("❌ Erreur lors de l'envoi du code !");
                return;
            }

            Stage resetStage = new Stage();
            new ResetPasswordInterface().start(resetStage, email);
        });

        // Hyperlink pour créer un compte
        Hyperlink signup = new Hyperlink("Créer un compte");
        signup.setTextFill(Color.AQUA);
        signup.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        signup.setStyle("-fx-underline: true;");
        signup.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            RegisterInterface registerInterface = new RegisterInterface();
            Stage registerStage = new Stage();
            registerStage.setScene(new Scene(registerInterface.getView(), 900, 600));
            registerStage.setTitle("Créer un compte");
            registerStage.show();
        });

        HBox links = new HBox(20);
        links.setAlignment(Pos.CENTER);
        links.getChildren().addAll(forgot, signup);

        form.getChildren().addAll(title, emailField, passwordField, loginBtn, errorLabel, links);
        root.getChildren().addAll(background, form);
    }

    private void styleInputField(TextField field) {
        field.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: aqua;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.5);" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 5;"
        );
    }

    public StackPane getView() {
        return root;
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
}
