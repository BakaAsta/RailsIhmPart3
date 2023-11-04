package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends VBox {

    private RailsIHM app;


    private final IJeu jeu;

    private Label joueurGagant = new Label();


    public VueResultats(IJeu jeu) {
        this.jeu = jeu;



        AtomicInteger cpt = new AtomicInteger(1);
        AtomicInteger cptListener = new AtomicInteger(1);

        AtomicReference<String> scoreActuel = new AtomicReference<>("");

        joueurGagant.setText(jeu.getJoueurs().stream().sorted((j1, j2) -> j2.getScore() - j1.getScore()).toArray(IJoueur[]::new)[0].getNom());

        System.out.println(jeu.getJoueurs().stream().sorted((j1, j2) -> j2.getScore() - j1.getScore()).toArray(IJoueur[]::new));

        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(10));

        Label labelTitre = new Label("Tableau des scores");
        labelTitre.setStyle("-fx-font-family: 'IM FELL English SC';" +
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;");
        labelTitre.setAlignment(javafx.geometry.Pos.CENTER);
        getChildren().add(labelTitre);


        for (IJoueur joueur : jeu.getJoueurs().stream().sorted((j1, j2) -> j2.getScore() - j1.getScore()).toArray(IJoueur[]::new)) {
            if (cpt.get() == 1) {

            }
            scoreActuel.set(joueur.getScore() + " points");
            HBox hboxJoueurActuel = new HBox();
            Label placeJoueurActuel = new Label();
            placeJoueurActuel.setText("Joueur " + cpt + " : ");
            placeJoueurActuel.setPadding(new javafx.geometry.Insets(10));
            placeJoueurActuel.setStyle("-fx-font-family: 'IM FELL English SC';" +
                    "-fx-font-size: 25px;" +
                    "-fx-font-weight: bold;");
            ImageView imageJoueurGagantActuel = new ImageView("file:src/main/resources/images/gagant.png");

            HBox texteAvecImageGagnant = new HBox();

            placeJoueurActuel.setText(cpt.get() + " : " );
            texteAvecImageGagnant.getChildren().add(placeJoueurActuel);

            Label joueurcac = new Label(joueur.getNom() + " : " + scoreActuel);
            texteAvecImageGagnant.getChildren().addAll(imageJoueurGagantActuel, joueurcac);

            hboxJoueurActuel.setStyle("-fx-background-color:white;" +
                    "-fx-border-color:"  + VueAutresJoueurs.mapCouleur(String.valueOf(joueur.getCouleur())) + ";"
                    + "-fx-border-width: 2px;" +
                    "-fx-border-radius: 10px;"
            + "-fx-background-radius: 20px;");
            cpt.getAndIncrement();

            hboxJoueurActuel.getChildren().add(placeJoueurActuel);
            getChildren().add(hboxJoueurActuel);
        }
        cpt.set(1);
        jeu.joueurCourantProperty().addListener((observable, oldValue, newValue) -> {
                cpt.set(1);
                getChildren().clear();
                getChildren().add(labelTitre);
                for (IJoueur joueur : jeu.getJoueurs().stream().sorted((j1, j2) -> j2.getScore() - j1.getScore()).toArray(IJoueur[]::new)) {
                    scoreActuel.set(joueur.getScore() + " points");
                    HBox hboxJoueurActuel = new HBox();
                    Label placeJoueurActuel = new Label();
                    placeJoueurActuel.setText("Joueur " + cpt + " : ");
                    placeJoueurActuel.setPadding(new javafx.geometry.Insets(10));
                    placeJoueurActuel.setStyle("-fx-font-family: 'IM FELL English SC';" +
                            "-fx-font-size: 25px;" +
                            "-fx-font-weight: bold;");
                    placeJoueurActuel.setText(cpt.get() + " : " + joueur.getNom() + " : " + scoreActuel);
                    hboxJoueurActuel.setStyle("-fx-background-color:white;" +
                            "-fx-border-color:"  + VueAutresJoueurs.mapCouleur(String.valueOf(joueur.getCouleur())) + ";"
                            + "-fx-border-width: 2px;" +
                            "-fx-border-radius: 10px;"
                            + "-fx-background-radius: 20px;");
                    cpt.getAndIncrement();

                    hboxJoueurActuel.getChildren().add(placeJoueurActuel);
                    getChildren().add(hboxJoueurActuel);
                }
        });
    }
}

