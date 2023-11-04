package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends HBox {
    private final IJeu jeu;


    public VueAutresJoueurs(IJeu jeu, IJoueur joueur) {
        this.jeu = jeu;

        for (IJoueur joueur1 : jeu.getJoueurs().stream().filter(c -> !c.equals(joueur)).toArray(IJoueur[]::new)) {
            Label label = new Label(joueur1.getNom());
            label.setStyle("-fx-font-family: 'IM FELL English SC';" + "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;" + "-fx-background-color:" + mapCouleur(String.valueOf(joueur1.getCouleur())) + ";" +
                    "-fx-background-radius: 10;");
            label.setPadding(new javafx.geometry.Insets(10));
            label.setBorder(new Border(new javafx.scene.layout.BorderStroke(Color.BLACK, javafx.scene.layout.BorderStrokeStyle.SOLID, new CornerRadii(10), new javafx.scene.layout.BorderWidths(2))));
            this.getChildren().add(label);
        }
        this.setBorder(new Border(new BorderStroke(Color.PINK, BorderStrokeStyle.SOLID, null, new javafx.scene.layout.BorderWidths(2))));
    }
    public static String mapCouleur(String couleurJoueur) {
        switch (couleurJoueur) {
            case "ROSE":
                return "#FFC0CB";
            case "BLEU":
                return "#00BFFF";
            case "VERT":
                return "#00FF00";
            case "JAUNE":
                return "#FFFF00";
            case "NOIR":
                return "#000000";
            case "ROUGE":
                return "#FF0000";
            case "BLANC":
                return "#FFFFFF";
            case "ORANGE":
                return "#FFA500";
            case "VIOLET":
                return "#800080";
            case "GRIS":
                return "#808080";
        }
        return couleurJoueur;
    }
}
