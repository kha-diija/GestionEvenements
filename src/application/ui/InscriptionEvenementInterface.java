package application.ui;

import application.dao.InscriptionDAO;
import application.models.InscriptionDetails;
import application.utils.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InscriptionEvenementInterface {

    private TableView<InscriptionDetails> table;
    private TextField searchField;
    private InscriptionDAO inscriptionDAO;
    private List<InscriptionDetails> allInscriptions = new ArrayList<>();

    public InscriptionEvenementInterface() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            inscriptionDAO = new InscriptionDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getView() {
        VBox mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setPadding(new Insets(30));
        mainPane.setStyle("-fx-background-color: #000000;");

        Text title = new Text("Inscriptions aux Événements");
        title.setFont(Font.font(24));
        title.setFill(javafx.scene.paint.Color.valueOf("#00FFFF"));
        title.setStyle("-fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom d’utilisateur ou d’événement...");
        searchField.setStyle("-fx-text-fill: #00FFFF; -fx-background-color: #000000; -fx-font-size: 14px; -fx-alignment: CENTER;");
        searchField.setOnKeyReleased(e -> filterList());

        table = new TableView<>();
        table.setStyle("-fx-background-color: #000000; -fx-border-color: #00FFFF; -fx-border-width: 2px;");
        table.setPrefWidth(590);
        table.setMaxWidth(608);

        TableColumn<InscriptionDetails, String> colEvenement = new TableColumn<>("Événement");
        colEvenement.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomEvenement()));
        colEvenement.setMinWidth(300);
        colEvenement.setStyle("-fx-alignment: CENTER;");

        TableColumn<InscriptionDetails, String> colUtilisateur = new TableColumn<>("Utilisateur");
        colUtilisateur.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomUtilisateur()));
        colUtilisateur.setMinWidth(300);
        colUtilisateur.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(colEvenement, colUtilisateur);

        // Charger toutes les inscriptions une fois
        allInscriptions = inscriptionDAO.getAllInscriptionsAvecDetails()
                .stream()
                .sorted((a, b) -> a.getNomEvenement().compareToIgnoreCase(b.getNomEvenement()))
                .collect(Collectors.toList());

        table.getItems().addAll(allInscriptions);

        StackPane tableContainer = new StackPane(table);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(20));

        mainPane.getChildren().addAll(title, searchField, tableContainer);
        return mainPane;
    }

    private void filterList() {
        String filter = searchField.getText().toLowerCase();

        table.getItems().clear();

        for (InscriptionDetails ins : allInscriptions) {
            if (ins.getNomEvenement().toLowerCase().contains(filter)
                    || ins.getNomUtilisateur().toLowerCase().contains(filter)) {
                table.getItems().add(ins);
            }
        }
    }

    private <T> void applyRowColor(TableColumn<InscriptionDetails, T> column) {
        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<InscriptionDetails, T> call(TableColumn<InscriptionDetails, T> param) {
                return new TableCell<>() {
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
                };
            }
        });
    }
}
