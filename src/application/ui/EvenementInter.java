package application.ui;

import application.dao.EvenementDAO;
import application.dao.InscriptionDAO;
import application.models.Evenements;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.List;

import application.utils.DatabaseConnection;

public class EvenementInter {

    private TableView<Evenements> table;
    private TextField searchField;
    private EvenementDAO evenementDAO;
    private InscriptionDAO inscriptionDAO;

    public EvenementInter() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            evenementDAO = new EvenementDAO(conn);
            inscriptionDAO = new InscriptionDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getView() {
        VBox eventPane = new VBox(20);
        eventPane.setAlignment(Pos.CENTER);
        eventPane.setPadding(new Insets(30));
        eventPane.setStyle("-fx-background-color: #000000;");

        Text title = new Text("Événements");
        title.setFont(Font.font(24));
        title.setFill(javafx.scene.paint.Color.valueOf("#00FFFF"));
        title.setStyle("-fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Rechercher par nom...");
        searchField.setStyle("-fx-text-fill: #00FFFF; -fx-background-color: #000000; -fx-font-size: 14px; -fx-alignment: CENTER;");
        searchField.setOnKeyReleased(e -> filterList());

        table = new TableView<>();
        table.setStyle("-fx-background-color: #000000; -fx-border-color: #00FFFF; -fx-border-width: 2px;");
        table.setStyle(
        	    "-fx-table-cell-border-color: transparent; " +
        	    "-fx-table-cell-border-color: #E0E0E0; " +
        	    "-fx-table-cell-hover-border-color: #00FFFF;");

        table.setPrefWidth(915);
        table.setMaxWidth(1015);

        TableColumn<Evenements, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colNom.setMinWidth(180);
        colNom.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colNom);

        TableColumn<Evenements, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDate.setMinWidth(135);
        colDate.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colDate);

        TableColumn<Evenements, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLieu()));
        colLieu.setMinWidth(125);
        colLieu.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colLieu);

        TableColumn<Evenements, Integer> colCapacite = new TableColumn<>("Capacité");
        colCapacite.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacite()).asObject());
        colCapacite.setMinWidth(90);
        colCapacite.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colCapacite);

        TableColumn<Evenements, Double> colPrix = new TableColumn<>("Prix (DH)");
        colPrix.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrix()).asObject());
        colPrix.setMinWidth(90);
        colPrix.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colPrix);

        TableColumn<Evenements, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        colDesc.setMinWidth(260);
        colDesc.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colDesc);

        TableColumn<Evenements, Integer> colPlacesRestantes = new TableColumn<>("Places restantes");
        colPlacesRestantes.setCellValueFactory(data -> {
            int total = data.getValue().getCapacite();
            int inscrits = evenementDAO.getNombreInscriptionsActivesPourEvenement(data.getValue().getId());
            return new SimpleIntegerProperty(total - inscrits).asObject();
        });
        colPlacesRestantes.setMinWidth(130);
        colPlacesRestantes.setStyle("-fx-alignment: CENTER;");
        applyRowColor(colPlacesRestantes);

        table.getColumns().addAll(colNom, colDate, colLieu, colCapacite, colPlacesRestantes, colPrix, colDesc);

        List<Evenements> evenements = evenementDAO.getAllEvenements();
        table.getItems().addAll(evenements);

        StackPane tableContainer = new StackPane(table);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(20));

        eventPane.getChildren().addAll(title, searchField, tableContainer);

        return eventPane;
    }

    private void filterList() {
        String filter = searchField.getText().toLowerCase();
        List<Evenements> allEvents = evenementDAO.getAllEvenements();
        table.getItems().clear();
        for (Evenements event : allEvents) {
            if (event.getNom().toLowerCase().contains(filter)) {
                table.getItems().add(event);
            }
        }
    }

    private <T> void applyRowColor(TableColumn<Evenements, T> column) {
        column.setCellFactory(param -> new TableCell<>() {
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
}
