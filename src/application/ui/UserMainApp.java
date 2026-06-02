package application.ui;

import application.dao.EvenementDAO;
import application.dao.UtilisateurDAO;
import application.models.utilisateurs;
import application.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;

public class UserMainApp extends Application {

    private StackPane contentPane;
    private Connection conn;

    @Override
    public void start(Stage primaryStage) {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("❌ Connexion échouée !");
            return;
        }

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
        EvenementDAO evenementDAO = new EvenementDAO(conn);

        // Menu latéral
        VBox sidebar = createSidebar(utilisateurDAO, evenementDAO);

        // Zone principale (image de bienvenue)
        contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: #000000;");
        HBox.setHgrow(contentPane, Priority.ALWAYS);

        Image accueilImage = new Image("file:/C:/Users/dell/Documents/eventuser.png"); // Choisis une autre image
        ImageView accueilImageView = new ImageView(accueilImage);

        accueilImageView.setPreserveRatio(true);
        accueilImageView.setSmooth(true);
        accueilImageView.setCache(true);

        accueilImageView.setFitWidth(1100); // Moins large que MainApp
        accueilImageView.fitHeightProperty().bind(contentPane.heightProperty().subtract(40));

        VBox imageContainer = new VBox(accueilImageView);
        imageContainer.setStyle("-fx-background-color: #000000;");
        imageContainer.setAlignment(Pos.TOP_CENTER);

        contentPane.getChildren().add(imageContainer);

        // Layout principal
        HBox root = new HBox(sidebar, contentPane);
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Espace Utilisateur - Gestion des événements");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar(UtilisateurDAO utilisateurDAO, EvenementDAO evenementDAO) {
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setSpacing(20);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #000000;");
        sidebar.setPrefWidth(220);

        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.TOP_CENTER);

        String[] sectionLabels = {
            "Événements",
            "Événements Disponibles",
            "Mes Événements",
            "Compte Utilisateur"
        };

        for (String label : sectionLabels) {
            Button button = new Button(label);
            button.setFont(Font.font(15));
            button.setPrefWidth(180);
            button.setPrefHeight(50);
            button.setStyle("-fx-background-color: #00CCCC; -fx-text-fill: white;");
            button.setOnAction(e -> changeContent(label, utilisateurDAO, evenementDAO));
            buttonContainer.getChildren().add(button);
        }

        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.setFont(Font.font(15));
        logoutBtn.setPrefWidth(180);
        logoutBtn.setPrefHeight(50);
        logoutBtn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> logout());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(buttonContainer, spacer, logoutBtn);
        return sidebar;
    }

    private void changeContent(String sectionName, UtilisateurDAO utilisateurDAO, EvenementDAO evenementDAO) {
        contentPane.getChildren().clear();

        switch (sectionName) {
            case "Événements" -> contentPane.getChildren().add(new EvenementInter().getView());
            case "Événements Disponibles" -> {
                int userId = getUserIdFromSession();
                contentPane.getChildren().add(new InterfaceEvenementsDispo(evenementDAO, userId).getView());
            }
            case "Mes Événements" -> contentPane.getChildren().add(new EvenementsUtilisateurInterface().getView()); // À créer
            case "Compte Utilisateur" -> {
                int userId = getUserIdFromSession();
                utilisateurs user = utilisateurDAO.getUtilisateurById(userId);
                if (user != null) {
                    contentPane.getChildren().add(new ProfilUtilisateurInterface(user, conn).getView());
                } else {
                    System.err.println("Utilisateur non trouvé.");
                }
            }
        }
    }

    private void logout() {
        Stage currentStage = (Stage) contentPane.getScene().getWindow();
        currentStage.close();

        LoginInterface loginInterface = new LoginInterface();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginInterface.getView(), 900, 600));
        loginStage.setTitle("Connexion");
        loginStage.show();
    }

    private int getUserIdFromSession() {
        return application.utils.Session.getCurrentUserId();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
