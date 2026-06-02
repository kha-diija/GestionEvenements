package application.ui;

import application.dao.EvenementDAO;
import application.models.Evenements;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class InterfaceEvenementsDispo {

    private final EvenementDAO evenementDAO;
    private final int idUtilisateurConnecte;
    private TableView<Evenements> table;
    private TextField searchField;

    public InterfaceEvenementsDispo(EvenementDAO evenementDAO, int idUtilisateurConnecte) {
        this.evenementDAO = evenementDAO;
        this.idUtilisateurConnecte = idUtilisateurConnecte;
    }

    public VBox getView() {
        VBox eventPane = new VBox(20);
        eventPane.setAlignment(Pos.CENTER);
        eventPane.setPadding(new Insets(30));
        eventPane.setStyle("-fx-background-color: #000000;");

        Text title = new Text("Événements Disponibles");
        title.setFont(Font.font(24));
        title.setFill(Color.valueOf("#00FFFF"));
        title.setStyle("-fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom...");
        searchField.setStyle("-fx-text-fill: #00FFFF; -fx-background-color: #000000; -fx-font-size: 14px; -fx-alignment: CENTER;");
        searchField.setOnKeyReleased(e -> filterListDisponibles());

        table = new TableView<>();
        table.setStyle("-fx-background-color: #000000; -fx-border-color: #00FFFF; -fx-border-width: 2px;");
        table.setPrefWidth(945);
        table.setMaxWidth(1045);

        TableColumn<Evenements, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colNom.setMinWidth(180);

        TableColumn<Evenements, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDate.setMinWidth(130);

        TableColumn<Evenements, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLieu()));
        colLieu.setMinWidth(130);

        TableColumn<Evenements, Integer> colCapacite = new TableColumn<>("Capacité");
        colCapacite.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacite()).asObject());
        colCapacite.setMinWidth(90);

        TableColumn<Evenements, Integer> colPlaces = new TableColumn<>("Places restantes");
        colPlaces.setCellValueFactory(data -> {
            int inscrits = evenementDAO.getNombreInscritsPourEvenement(data.getValue().getId());
            int placesRestantes = data.getValue().getCapacite() - inscrits;
            return new SimpleIntegerProperty(placesRestantes).asObject();
        });
        colPlaces.setMinWidth(110);

        TableColumn<Evenements, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        colDescription.setMinWidth(290);
        colDescription.setCellFactory(tc -> new TableCell<>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String desc, boolean empty) {
                super.updateItem(desc, empty);
                if (empty || desc == null) {
                    setGraphic(null);
                } else {
                    label.setText(desc.length() > 50 ? desc.substring(0, 50) + "..." : desc);
                    label.setTooltip(new Tooltip(desc));
                    setGraphic(label);
                }
            }
        });

        TableColumn<Evenements, Void> colAction = new TableColumn<>("Action");
        colAction.setMinWidth(100);
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("S'inscrire");

            {
                btn.setStyle("-fx-background-color: #00FFFF; -fx-text-fill: black; -fx-font-weight: bold;");
                btn.setOnAction(event -> {
                    btn.setDisable(true); // ⛔ empêcher double-clic immédiat
                    Evenements selected = getTableView().getItems().get(getIndex());

                    // Exécuter dans un thread pour éviter de bloquer l'interface
                    new Thread(() -> {
                        boolean dejaInscrit = evenementDAO.estDejaInscrit(idUtilisateurConnecte, selected.getId());

                        int inscrits = evenementDAO.getNombreInscritsPourEvenement(selected.getId());
                        int placesRestantes = selected.getCapacite() - inscrits;

                        if (dejaInscrit) {
                            showAlertFxThread(Alert.AlertType.INFORMATION, "Déjà inscrit", "Vous êtes déjà inscrit à cet événement.");
                        } else if (placesRestantes <= 0) {
                            showAlertFxThread(Alert.AlertType.WARNING, "Complet", "Plus de places disponibles pour cet événement.");
                        } else {
                            boolean success = evenementDAO.ajouterInscription(idUtilisateurConnecte, selected.getId());
                            if (success) {
                                evenementDAO.ajouterConfirmation(idUtilisateurConnecte, selected.getId());
                                showAlertFxThread(Alert.AlertType.INFORMATION, "Succès 🎉", "Inscription réussie à l'événement !");
                            }

 else {
                                showAlertFxThread(Alert.AlertType.ERROR, "Erreur", "Échec de l'inscription.");
                            }
                        }

                        // Mise à jour de la table dans le thread JavaFX
                        javafx.application.Platform.runLater(() -> {
                            filterListDisponibles();
                            btn.setDisable(false); // 🔓 Réactiver après
                        });
                    }).start();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Evenements event = getTableView().getItems().get(getIndex());
                    int inscrits = evenementDAO.getNombreInscritsPourEvenement(event.getId());
                    int placesRestantes = event.getCapacite() - inscrits;

                    if (evenementDAO.estDejaInscrit(idUtilisateurConnecte, event.getId())) {
                        setGraphic(new Label("Inscrit"));
                    } else if (placesRestantes <= 0) {
                        setGraphic(new Label("Complet"));
                    } else {
                        btn.setDisable(false); // reset button status
                        setGraphic(btn);
                    }
                }
            }
        });


        table.getColumns().addAll(colNom, colDate, colLieu, colCapacite, colPlaces, colDescription, colAction);
        loadEvenements();

        StackPane tableContainer = new StackPane(table);
        tableContainer.setPadding(new Insets(20));

        eventPane.getChildren().addAll(title, searchField, tableContainer);
        return eventPane;
    }

    private void loadEvenements() {
        table.getItems().clear();
        List<Evenements> evenements = evenementDAO.getEvenementsDisponibles();
        table.getItems().addAll(evenements);
    }

    private void filterListDisponibles() {
        String filter = searchField.getText().toLowerCase();
        List<Evenements> allEvents = evenementDAO.getEvenementsDisponibles();
        table.getItems().clear();
        for (Evenements event : allEvents) {
            if (event.getNom().toLowerCase().contains(filter)) {
                table.getItems().add(event);
            }
        }
    }

    
    private void showAlertFxThread(Alert.AlertType type, String titre, String message) {
        javafx.application.Platform.runLater(() -> {
            showAlert(type, titre, message);
        });
    }

    
    
    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #00FFFF;");
        alert.showAndWait();
    }
}
