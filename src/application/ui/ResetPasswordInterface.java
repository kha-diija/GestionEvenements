package application.ui;

import application.dao.UtilisateurDAO;
import application.utils.DatabaseConnection;
import application.utils.EmailUtil;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;

public class ResetPasswordInterface {

    private String generatedCode;

    public void start(Stage stage, String email) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30; -fx-background-color: #1e1e1e;");

        TextField codeField = new TextField();
        codeField.setPromptText("Code de réinitialisation");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nouveau mot de passe");

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirmer le mot de passe");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        ChangeListener<String> pwdListener = (obs, oldVal, newVal) -> {
            if (newPasswordField.getText().equals(confirmField.getText())) {
                message.setText("");
            }
        };
        newPasswordField.textProperty().addListener(pwdListener);
        confirmField.textProperty().addListener(pwdListener);

        Button confirmBtn = new Button("Confirmer");

        // 🔐 Génère et envoie le code
        generatedCode = EmailUtil.generateResetCode();
        try {
            EmailUtil.sendResetCode(email, generatedCode);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de l'envoi de l'email. Vérifiez votre connexion.", Alert.AlertType.ERROR);
            return;
        }

        confirmBtn.setOnAction(e -> {
            String codeEntered = codeField.getText();
            String newPwd = newPasswordField.getText();
            String confirmPwd = confirmField.getText();

            if (!codeEntered.equals(generatedCode)) {
                message.setText("❌ Code invalide !");
                return;
            }

            if (!newPwd.equals(confirmPwd)) {
                message.setText("❌ Les mots de passe ne correspondent pas !");
                return;
            }

            // Mise à jour via DAO
            try {
                Connection conn = DatabaseConnection.getConnection();
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
                boolean updated = utilisateurDAO.updatePasswordByEmail(email, newPwd);
                if (updated) {
                    showAlert("Succès", "✅ Mot de passe modifié avec succès !", Alert.AlertType.INFORMATION);
                    stage.close();
                } else {
                    showAlert("Erreur", "❌ Utilisateur introuvable !", Alert.AlertType.ERROR);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur", "❌ Erreur lors de la mise à jour.", Alert.AlertType.ERROR);
            }
        });

        root.getChildren().addAll(codeField, newPasswordField, confirmField, confirmBtn, message);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
