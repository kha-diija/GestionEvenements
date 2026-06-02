package application.ui;

import application.dao.EvenementDAO;
import application.models.Evenements;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class InterfaceEvenementsDisponibles {

    private final EvenementDAO evenementDAO;
    private TableView<Evenements> table;
    private TextField searchField;

    public InterfaceEvenementsDisponibles(EvenementDAO evenementDAO) {
        this.evenementDAO = evenementDAO;
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
        table.setPrefWidth(1040);
        table.setMaxWidth(1065);

        TableColumn<Evenements, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colNom.setMinWidth(180);
        colNom.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDate.setMinWidth(135);
        colDate.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, String> colLieu = new TableColumn<>("Lieu");
        colLieu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLieu()));
        colLieu.setMinWidth(135);
        colLieu.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, Integer> colCapacite = new TableColumn<>("Capacité");
        colCapacite.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacite()).asObject());
        colCapacite.setMinWidth(90);
        colCapacite.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, Integer> colPlacesRestantes = new TableColumn<>("Places restantes");
        colPlacesRestantes.setCellValueFactory(data -> {
            int total = data.getValue().getCapacite();
            int inscrits = evenementDAO.getNombreInscriptionsActivesPourEvenement(data.getValue().getId());
            return new SimpleIntegerProperty(total - inscrits).asObject();
        });
        colPlacesRestantes.setMinWidth(120);
        colPlacesRestantes.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, Double> colPrix = new TableColumn<>("Prix (DH)");
        colPrix.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrix()).asObject());
        colPrix.setMinWidth(100);
        colPrix.setStyle("-fx-alignment: CENTER;");

        TableColumn<Evenements, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        colDescription.setMinWidth(290);
        colDescription.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(colNom, colDate, colLieu, colCapacite, colPlacesRestantes, colPrix, colDescription);

        List<Evenements> evenementsDisponibles = evenementDAO.getEvenementsDisponibles();
        table.getItems().addAll(evenementsDisponibles);

        StackPane tableContainer = new StackPane(table);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(20));

        eventPane.getChildren().addAll(title, searchField, tableContainer);

        return eventPane;
    }

    private void filterListDisponibles() {
        String filter = searchField.getText().toLowerCase();
        List<Evenements> allEventsDisponibles = evenementDAO.getEvenementsDisponibles();
        table.getItems().clear();
        for (Evenements event : allEventsDisponibles) {
            if (event.getNom().toLowerCase().contains(filter)) {
                table.getItems().add(event);
            }
        }
    }
}
