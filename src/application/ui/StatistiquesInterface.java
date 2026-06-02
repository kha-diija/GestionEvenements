package application.ui;

import application.dao.StatistiquesDAO;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.sql.Connection;

public class StatistiquesInterface {

    private VBox view;
    private final StatistiquesDAO statistiquesDAO;

    public StatistiquesInterface(Connection conn) {
        this.statistiquesDAO = new StatistiquesDAO(conn);
        buildUI();
    }

    private void buildUI() {
        view = new VBox(40);
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #2B2B2B;");
        view.setPadding(new javafx.geometry.Insets(30));

        int totalUsers = statistiquesDAO.getNombreUtilisateurs();
        int totalEvents = statistiquesDAO.getNombreEvenements();
        int usersActifs = statistiquesDAO.getNombreUtilisateursInscrits();
        int usersInactifs = totalUsers - usersActifs;

        int evenementsReserves = statistiquesDAO.getNombreEvenementsReserves();
        double pourcentageReserves = totalEvents == 0 ? 0 : (evenementsReserves * 100.0) / totalEvents;

        double pourcentageActifs = totalUsers == 0 ? 0 : (usersActifs * 100.0) / totalUsers;
        double pourcentageInactifs = 100.0 - pourcentageActifs;

        // Ligne contenant les pourcentages
        HBox ligne1 = new HBox(40,
                animatedStatCircle("Utilisateurs actifs", pourcentageActifs),
                animatedStatCircle("Utilisateurs inactifs", pourcentageInactifs),
                animatedStatCircle("Réservations événements", pourcentageReserves)
        );
        ligne1.setAlignment(Pos.CENTER);

        // Ligne contenant les nombres
        HBox ligne2 = new HBox(40,
                numberCounter("Utilisateurs", totalUsers),
                numberCounter("Événements", totalEvents)
        );
        ligne2.setAlignment(Pos.CENTER);

        // Ajouter les lignes et bouton d'exportation
        view.getChildren().addAll(ligne1, ligne2);
    }

    private VBox animatedStatCircle(String label, double pourcentage) {
        double radius = 80;
        double stroke = 18;

        Arc background = new Arc(0, 0, radius, radius, 90, 360);
        background.setType(ArcType.OPEN);
        background.setStroke(Color.GRAY);
        background.setStrokeWidth(stroke);
        background.setFill(null);

        Arc progress = new Arc(0, 0, radius, radius, 90, 0);
        progress.setType(ArcType.OPEN);
        progress.setStroke(Color.AQUA);
        progress.setStrokeWidth(stroke);
        progress.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        progress.setFill(null);

        Timeline progressAnim = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(progress.lengthProperty(), -pourcentage * 3.6))
        );
        progressAnim.play();

        Label percentLabel = new Label(String.format("%.1f%%", pourcentage));
        percentLabel.setFont(Font.font(18));
        percentLabel.setTextFill(Color.AQUA);

        Label title = new Label(label);
        title.setFont(Font.font(14));
        title.setTextFill(Color.WHITE);

        Group circle = new Group(background, progress);
        VBox container = new VBox(10, circle, percentLabel, title);
        container.setAlignment(Pos.CENTER);
        container.setScaleX(0);
        container.setScaleY(0);

        ScaleTransition st = new ScaleTransition(Duration.millis(700), container);
        st.setToX(1);
        st.setToY(1);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();

        return container;
    }

    private VBox numberCounter(String label, int target) {
        Label numberLabel = new Label(String.valueOf(target));
        numberLabel.setFont(Font.font(40));  // Augmenter la taille du texte
        numberLabel.setTextFill(Color.AQUA); // Couleur en aqua

        Label title = new Label(label);
        title.setFont(Font.font(14));
        title.setTextFill(Color.WHITE);

        VBox container = new VBox(10, numberLabel, title);
        container.setAlignment(Pos.CENTER);
        container.setScaleX(0);
        container.setScaleY(0);

        ScaleTransition st = new ScaleTransition(Duration.millis(700), container);
        st.setToX(1);
        st.setToY(1);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();

        return container;
    }

    public Node getView() {
        return view;
    }
}
