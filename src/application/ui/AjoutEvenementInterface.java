package application.ui;

import application.dao.EvenementDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

public class AjoutEvenementInterface {
    private final EvenementDAO evenementDAO;
    private VBox view;

    public AjoutEvenementInterface(EvenementDAO evenementDAO) {
        this.evenementDAO = evenementDAO;
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

        Text title = new Text("Ajouter un événement");
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
        Label nomLabel = createLabel("Nom de l'événement");
        TextField nomField = createTextField("Nom de l'événement");

        Label lieuLabel = createLabel("Lieu");
        TextField lieuField = createTextField("Lieu");

        Label capaciteLabel = createLabel("Capacité");
        TextField capaciteField = createTextField("Capacité");

        Label descriptionLabel = createLabel("Description");
        TextArea descriptionField = createTextArea("Description");

        Label dateLabel = createLabel("Date de l'événement");
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(350);
        datePicker.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Button saveButton = new Button("Ajouter l'événement");
        saveButton.setStyle(
            "-fx-background-color: #00CCCC;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 30 10 30;" +
            "-fx-background-radius: 10;"
        );

        saveButton.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String description = descriptionField.getText().trim();
            String lieu = lieuField.getText().trim();
            String capaciteText = capaciteField.getText().trim();

            if (nom.isEmpty() || description.isEmpty() || lieu.isEmpty() || capaciteText.isEmpty() || datePicker.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Vérifier si la capacité est un nombre entier valide
            int capacite = 0;
            try {
                capacite = Integer.parseInt(capaciteText);
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer un nombre valide pour la capacité.");
                return;
            }

            // Vérifier si un événement avec le même nom existe déjà
            if (evenementDAO.evenementExiste(nom)) {
                showAlert("Erreur", "Un événement avec ce nom existe déjà.");
                return;
            }

            evenementDAO.ajouterEvenement(nom, description, datePicker.getValue(), lieu, capacite);
            showAlert("Succès", "Événement ajouté avec succès !");
            nomField.clear();
            descriptionField.clear();
            lieuField.clear();
            capaciteField.clear();
            datePicker.setValue(null);
        });


        HBox buttonContainer = new HBox(saveButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));

        formContainer.getChildren().addAll(
            nomLabel, nomField,
            lieuLabel, lieuField,
            capaciteLabel, capaciteField,
            descriptionLabel, descriptionField,
            dateLabel, datePicker,
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

    private TextArea createTextArea(String prompt) {
        TextArea field = new TextArea();
        field.setPromptText(prompt);
        field.setPrefWidth(350);
        field.setPrefHeight(100);  // Hauteur définie
        field.setStyle("-fx-background-color: #333; -fx-text-fill: black; -fx-border-color: transparent;");
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
