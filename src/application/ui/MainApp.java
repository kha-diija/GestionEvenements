package application.ui;

import application.dao.UtilisateurDAO;
import application.dao.EvenementDAO;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;

public class MainApp extends Application {

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

        // Zone principale (image plein écran)
        contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: #000000;");
        HBox.setHgrow(contentPane, Priority.ALWAYS);

        Image bienvenue = new Image("file:/C:/Users/dell/Documents/bienvenueevent.png");
        ImageView bienvenueImage = new ImageView(bienvenue);

        // Conserver le ratio
        bienvenueImage.setPreserveRatio(true);
        bienvenueImage.setSmooth(true);
        bienvenueImage.setCache(true);

        // Container centré pour l'image
        VBox imageContainer = new VBox(bienvenueImage);
        imageContainer.setStyle("-fx-background-color: #000000;");
        imageContainer.setAlignment(Pos.TOP_CENTER);


        // Définir une largeur fixe plus petite, et une grande hauteur
        bienvenueImage.setFitWidth(1090); 
       // Moins de largeur
        bienvenueImage.setStyle("-fx-background-color: #000000;");
        bienvenueImage.fitHeightProperty().bind(contentPane.heightProperty().subtract(40)); // Hauteur dynamique (presque toute la hauteur)

        // Afficher l'image par défaut
        contentPane.getChildren().add(imageContainer);




        // Layout principal
        HBox root = new HBox(sidebar, contentPane);
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Gestion des événements");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar(UtilisateurDAO utilisateurDAO, EvenementDAO evenementDAO) {
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setSpacing(20); // plus d'espace entre les boutons
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #000000;");
        sidebar.setPrefWidth(220);

        VBox buttonContainer = new VBox(20); // Espacement entre les boutons
        buttonContainer.setAlignment(Pos.TOP_CENTER);

        String[] sectionLabels = {
            "Utilisateurs",
            "Ajouter Utilisateur",
            "Événements",
            "Événements Disponibles",
            "Ajouter Événement",
            "Inscription/evenement",
            "Inscription",
            "Statistiques",
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

        // Bouton de déconnexion en bas
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
            case "Dashboard" -> showDashboard();
            case "Utilisateurs" -> showUserInterface(utilisateurDAO);
            case "Événements" -> showEvenementInterface();
            case "Statistiques" -> contentPane.getChildren().add(new StatistiquesInterface(conn).getView());
            case "Inscription/evenement" -> showInscriptionEvenementInterface();
            case "Inscription" -> showInscriptionForm();
            case "Ajouter Utilisateur" -> showAjoutUtilisateur(utilisateurDAO);
            case "Ajouter Événement" -> showAjoutEvenement(evenementDAO);
            case "Compte Utilisateur" -> showCompteUtilisateur(utilisateurDAO);
            case "Événements Disponibles" -> contentPane.getChildren().add(new InterfaceEvenementsDisponibles(evenementDAO).getView());
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

    private void showDashboard() {
        Text dashboardText = new Text("Tableau de bord - Vue générale");
        dashboardText.setFont(Font.font(20));
        dashboardText.setStyle("-fx-fill: #009696;");
        contentPane.getChildren().add(dashboardText);
    }

    private void showUserInterface(UtilisateurDAO utilisateurDAO) {
        contentPane.getChildren().add(new UtilisateurInterface(utilisateurDAO).getView());
    }

    private void showEvenementInterface() {
        contentPane.getChildren().add(new EvenementInterface().getView());
    }

    private void showInscriptionEvenementInterface() {
        contentPane.getChildren().add(new InscriptionEvenementInterface().getView());
    }

    private void showInscriptionForm() {
        contentPane.getChildren().add(new InscriptionFormInterface(conn).getView());
    }

    private void showAjoutUtilisateur(UtilisateurDAO utilisateurDAO) {
        contentPane.getChildren().add(new AjoutUtilisateurInterface(utilisateurDAO).getView());
    }

    private void showAjoutEvenement(EvenementDAO evenementDAO) {
        contentPane.getChildren().add(new AjoutEvenementInterface(evenementDAO).getView());
    }

    private void showCompteUtilisateur(UtilisateurDAO utilisateurDAO) {
        int userId = getUserIdFromSession();
        utilisateurs utilisateurConnecte = utilisateurDAO.getUtilisateurById(userId);

        if (utilisateurConnecte != null) {
            ProfilUtilisateurInterface profilUtilisateurInterface = new ProfilUtilisateurInterface(utilisateurConnecte, conn);
            contentPane.getChildren().add(profilUtilisateurInterface.getView());
        } else {
            showAlert("Erreur", "Utilisateur non trouvé.");
        }
    }

    private int getUserIdFromSession() {
        return application.utils.Session.getCurrentUserId();
    }


    private void showAlert(String title, String message) {
        System.out.println(title + ": " + message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
