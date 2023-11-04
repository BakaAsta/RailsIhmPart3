package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends VBox {

    private final ICarteTransport carteTransport;

    private IntegerProperty nbCartes;

    ArrayList<CarteTransport> cartesTransportJoueur = new ArrayList<>();

    private final IJeu jeu;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes, IJeu jeu, double coeefficient) {
        this.carteTransport = carteTransport;
        this.jeu = jeu;

        jeu.joueurCourantProperty().addListener((observable, oldValue, newValue) -> {
            cartesTransportJoueur.addAll((Collection<? extends CarteTransport>) newValue.getCartesTransport());
            //this.nbCartes.setValue(newValue.getCartesTransport().stream().filter(carteTransport1 -> carteTransport1.equals(this.carteTransport)).collect(Collectors.toSet()).size());
        });

        String typeCarte = "";
        if (carteTransport.estWagon()) {
            typeCarte = "WAGON";
        } else if (carteTransport.estBateau()) {
            typeCarte = "BATEAU";
        } else if (carteTransport.estJoker()) {
            typeCarte = "JOKER";
        }
        if (carteTransport.estDouble()) {
            typeCarte += "DOUBLE";
        }
        String estAncre = "";
        if (carteTransport.getAncre()) {
            estAncre = "-A";
        }

        if (carteTransport.estDouble()) {
            typeCarte = new String("DOUBLE");
        }
        String imagePath = "file:src/main/resources/images/cartesWagons/carte-" + typeCarte + "-" + carteTransport.getStringCouleur() + estAncre + ".png";


        Image imageCarteTransport = new Image(imagePath);
        imageCarteTransport.isPreserveRatio();
        ImageView imageViewCarteTransport = new ImageView(imageCarteTransport);



        imageViewCarteTransport.setFitHeight(50 + coeefficient);
        imageViewCarteTransport.setFitWidth(80 + coeefficient);


        // 320  * 200


        HBox hboxCarteTransport = new HBox();
        Label labelNbCartes = new Label(String.valueOf(nbCartes));

        Circle circle = new Circle(8);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, labelNbCartes);


        hboxCarteTransport.getChildren().add(stackPane);
        hboxCarteTransport.getChildren().add(imageViewCarteTransport);

        this.setOnMouseClicked(event -> {
            jeu.uneCarteDuJoueurEstJouee(carteTransport);
        });
        this.getChildren().add(hboxCarteTransport);
    }

    public int getNbCartes() {
        return nbCartes.get();
    }

    public IntegerProperty nbCartesProperty() {
        return nbCartes;
    }




    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

}
