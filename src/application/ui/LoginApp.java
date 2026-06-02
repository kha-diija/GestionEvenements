package application.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginInterface loginInterface = new LoginInterface();

        // Spécifie l'action à effectuer après la connexion réussie
        loginInterface.setOnLoginSuccess(() -> {
            primaryStage.close(); // Fermer la fenêtre de login
            try {
                new MainApp().start(new Stage()); // Lancer MainApp
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(loginInterface.getView(), 400, 300); // ou taille selon ton design
        primaryStage.setTitle("Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
