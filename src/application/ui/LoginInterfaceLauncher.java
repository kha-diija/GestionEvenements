package application.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginInterfaceLauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginInterface loginInterface = new LoginInterface();
        Scene scene = new Scene(loginInterface.getView(), 900, 600);
        primaryStage.setTitle("Connexion");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
