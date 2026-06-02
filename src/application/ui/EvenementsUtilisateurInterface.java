package application.ui;

import application.dao.InscriptionDAO;
import application.models.EvenementReserve;
import application.utils.DatabaseConnection;
import application.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.util.List;

public class EvenementsUtilisateurInterface {

    private final TableView<EvenementReserve> tableView;
    private final Connection conn;
    private FilteredList<EvenementReserve> filteredList;
    private final TextField searchField;

    public EvenementsUtilisateurInterface() {
        conn = DatabaseConnection.getConnection();
        tableView = new TableView<>();
        searchField = new TextField();
        setupTable();
    }

    private void setupTable() {
        TableColumn<EvenementReserve, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<EvenementReserve, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<EvenementReserve, String> lieuCol = new TableColumn<>("Lieu");
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        TableColumn<EvenementReserve, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<EvenementReserve, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setStyle("-fx-background-color: #00CED1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                deleteButton.setOnAction(event -> {
                    EvenementReserve selected = getTableView().getItems().get(getIndex());
                    InscriptionDAO dao = new InscriptionDAO(conn);
                    boolean success = dao.supprimerInscription(Session.getCurrentUserId(), selected.getId());

                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Suppression", "Inscription supprimée avec succès.");
                        refreshEvents();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        tableView.getColumns().addAll(nomCol, dateCol, lieuCol, descCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        tableView.setStyle(
        	    "-fx-table-cell-border-color: transparent; " +
        	    "-fx-table-cell-border-color: #E0E0E0; " +
        	    "-fx-table-cell-hover-border-color: #00FFFF;");
        tableView.setPrefWidth(1005);
        tableView.setMaxWidth(1055);

        loadUserEvents();

        // Champ de recherche stylisé
        searchField.setPromptText("Rechercher par nom...");
        searchField.setStyle("-fx-background-color: #000000;  -fx-text-fill: #00FFFF; -fx-font-size: 14px; -fx-alignment: CENTER;");
        searchField.setMaxWidth(300);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(event ->
                newVal == null || newVal.isEmpty() ||
                event.getNom().toLowerCase().contains(newVal.toLowerCase()));
        });
    }

    private void loadUserEvents() {
        int userId = Session.getCurrentUserId();
        InscriptionDAO dao = new InscriptionDAO(conn);
        List<EvenementReserve> evenements = dao.getEvenementsReservesParUtilisateur(userId);

        ObservableList<EvenementReserve> data = FXCollections.observableArrayList(evenements);
        filteredList = new FilteredList<>(data, p -> true);
        tableView.setItems(filteredList);
    }

    private void refreshEvents() {
        loadUserEvents();
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER); // Centrage vertical et horizontal
        layout.setStyle("-fx-background-color: #000000;");

        Label titre = new Label("Mes Événements Réservés");
        titre.setFont(Font.font("Arial", 24));
        titre.setStyle("-fx-font-weight: bold; -fx-text-fill: #00FFFF; -fx-alignment: CENTER;");

        layout.getChildren().addAll(titre, searchField, tableView);
        return layout;
    }
}
