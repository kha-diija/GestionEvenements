package application.ui;

import application.dao.UtilisateurDAO;
import application.models.utilisateurs;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.List;

public class UtilisateurInterface {

    private TableView<utilisateurs> table;
    private TextField searchField;
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurInterface(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    public VBox getView() {
        VBox userPane = new VBox(20);
        userPane.setAlignment(Pos.CENTER);
        userPane.setPadding(new Insets(30));
        userPane.setStyle("-fx-background-color: #000000;");

        // Titre
        Text title = new Text("Utilisateurs");
        title.setFont(Font.font(24));
        title.setFill(javafx.scene.paint.Color.valueOf("#00FFFF"));
        title.setStyle("-fx-font-weight: bold;");

        // Barre de recherche
        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom...");
        searchField.setStyle("-fx-text-fill: #00FFFF; -fx-background-color: #000000; -fx-font-size: 14px;-fx-alignment: CENTER;");
        searchField.setOnKeyReleased(e -> filterList());

        // Tableau
        table = new TableView<>();
        table.setStyle("-fx-background-color: #000000; -fx-border-color: #00FFFF; -fx-border-width: 2px; -fx-table-cell-border-color: #000000;");
        table.setStyle(
        	    "-fx-table-cell-border-color: transparent; " +
        	    "-fx-table-cell-border-color: #E0E0E0; " +
        	    "-fx-table-cell-hover-border-color: #00FFFF;");


        table.setPrefWidth(1005);
        table.setMaxWidth(1025);

        // Colonnes
        TableColumn<utilisateurs, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colNom.setMinWidth(160);
        colNom.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");
        applyRowColor(colNom);

        TableColumn<utilisateurs, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colEmail.setMinWidth(220);
        colEmail.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");
        applyRowColor(colEmail);

        TableColumn<utilisateurs, String> colDateNaissance = new TableColumn<>("Date de Naissance");
        colDateNaissance.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDateNaissance() != null ? formatDate(data.getValue().getDateNaissance()) : ""
        ));
        colDateNaissance.setMinWidth(160);
        colDateNaissance.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");
        applyRowColor(colDateNaissance);

        TableColumn<utilisateurs, String> colDateInscription = new TableColumn<>("Date d'Inscription");
        colDateInscription.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDateInscription() != null ? formatDate(data.getValue().getDateInscription()) : ""
        ));
        colDateInscription.setMinWidth(160);
        colDateInscription.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");
        applyRowColor(colDateInscription);

        TableColumn<utilisateurs, String> colNbEvents = new TableColumn<>("Événements Réservés");
        colNbEvents.setCellValueFactory(data -> {
            int nb = data.getValue().getNbEvenements();
            return new SimpleStringProperty(nb > 0 ? String.valueOf(nb) : "Aucune réservation");
        });
        colNbEvents.setMinWidth(170);
        colNbEvents.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");
        applyRowColor(colNbEvents);
        
        TableColumn<utilisateurs, Void> colActions = new TableColumn<>("Action");
        colActions.setMinWidth(150);
        colActions.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold; -fx-alignment: CENTER;");

        Callback<TableColumn<utilisateurs, Void>, TableCell<utilisateurs, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<utilisateurs, Void> call(final TableColumn<utilisateurs, Void> param) {
                return new TableCell<>() {

                    private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Supprimer");

                    {
                        btn.setStyle("-fx-background-color: #00B2B2; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                        btn.setOnAction(event -> {
                            utilisateurs user = getTableView().getItems().get(getIndex());
                            boolean deleted = utilisateurDAO.supprimerUtilisateurParId(user.getId());
                            if (deleted) {
                                table.getItems().remove(user); // Met à jour la table
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colActions.setCellFactory(cellFactory);

        // Ordre final des colonnes
        table.getColumns().setAll(colNom, colEmail, colDateNaissance, colDateInscription, colNbEvents, colActions);

        
        // Charger les données
        table.getItems().addAll(utilisateurDAO.getUtilisateursWithReservationCount());

        // Layout
        StackPane tableContainer = new StackPane(table);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(20));

        userPane.getChildren().addAll(title, searchField, tableContainer);
        return userPane;
    }

    private void filterList() {
        String filter = searchField.getText().toLowerCase();
        List<utilisateurs> utilisateursList = utilisateurDAO.getUtilisateursWithReservationCount();
        table.getItems().clear();
        for (utilisateurs user : utilisateursList) {
            if (user.getNom().toLowerCase().contains(filter)) {
                table.getItems().add(user);
            }
        }
    }

    private <T> void applyRowColor(TableColumn<utilisateurs, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item == null ? "" : item.toString());
                    setStyle("-fx-text-fill: #00B2B2; -fx-background-color: transparent;");
                }
            }
        });
    }

    private String formatDate(java.util.Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}
