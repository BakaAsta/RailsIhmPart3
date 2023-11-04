package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.IRoute;
import fr.umontpellier.iut.rails.IVille;
import fr.umontpellier.iut.rails.mecanique.Route;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.umontpellier.iut.rails.vues.VueJoueurCourant.mapCouleur;

/**
 * Cette classe présente les routes et les villes sur le plateau.
 *
 * On y définit les handlers à exécuter lorsque qu'un élément du plateau a été choisi par l'utilisateur
 * ainsi que les bindings qui mettront à jour le plateau après la prise d'une route ou d'un port par un joueur
 */
public class VuePlateau extends Pane {

    @FXML
    private ImageView mapMonde;

    public VuePlateau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/plateau.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMaxSize(Screen.getPrimary().getBounds().getWidth()/3, Screen.getPrimary().getBounds().getHeight()/3);
    }

    EventHandler<MouseEvent> choixRoute = event -> {
        System.out.println("On a cliqué sur une route");
    };

    EventHandler<MouseEvent> choixPort = event -> {
        System.out.println("On a cliqué sur un port");
    };

    public void creerBindings() {
        ajoutPorts();
        ajoutRoutes();
        bindRedimensionEtCentragePlateau();
    }

    private void ajoutPorts() {
        for (String nomPort : DonneesGraphiques.ports.keySet()) {
            DonneesGraphiques.DonneesCerclesPorts positionPortSurPlateau = DonneesGraphiques.ports.get(nomPort);
            Circle cerclePort = new Circle(positionPortSurPlateau.centreX(), positionPortSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
            cerclePort.setId(nomPort);
            getChildren().add(cerclePort);
            cerclePort.setFill(Color.TRANSPARENT);
            bindCerclePortAuPlateau(positionPortSurPlateau, cerclePort);
            cerclePort.setOnMouseClicked(event -> {
                System.out.println("On a cliqué sur un port");
                ((VueDuJeu) getScene().getRoot()).getJeu().unPortAEteChoisi(nomPort);
            });
            System.out.println(((VueDuJeu) getScene().getRoot()).getJeu().getPorts());
            for (IVille ville : ((VueDuJeu) getScene().getRoot()).getJeu().getPorts()) {
                System.out.println();
                System.out.println(ville.getNom() + " : " + ville.getNom().equals(nomPort) + " : " + "nomPort : " + nomPort);
                if (ville.getNom().equals(nomPort)) {
                    ville.proprietaireProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            portsPrisChangementCouleur(nomPort, newValue);
                        }
                    });
                }
            }
        }
    }

    public void portsPrisChangementCouleur(String nomPort, IJoueur joueur) {
        DonneesGraphiques.DonneesCerclesPorts positionPortSurPlateau = DonneesGraphiques.ports.get(nomPort);
        System.out.println("Entrez changement couleur port");
        System.out.println(DonneesGraphiques.ports.get(nomPort));
        Circle cerclePort = new Circle(positionPortSurPlateau.centreX(), positionPortSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
        cerclePort.setId(nomPort);
        cerclePort.setFill(Color.web(mapCouleur(String.valueOf(joueur.getCouleur()))));
        cerclePort.setStroke(Color.BLACK);
        bindCerclePortAuPlateau(positionPortSurPlateau, cerclePort);
        cerclePort.setOnMouseClicked(choixPort);
        getChildren().add(cerclePort);
    }

    private void ajoutRoutes() {
        List<? extends IRoute> listeRoutes = ((VueDuJeu) getScene().getRoot()).getJeu().getRoutes();
        for (String nomRoute : DonneesGraphiques.routes.keySet()) {
            ArrayList<DonneesGraphiques.DonneesSegments> segmentsRoute = DonneesGraphiques.routes.get(nomRoute);
            IRoute route = listeRoutes.stream().filter(r -> r.getNom().equals(nomRoute)).findAny().orElse(null);
            route.proprietaireProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    routesPriseChangementCouleur(route, newValue);
                }
            });
            for (DonneesGraphiques.DonneesSegments unSegment : segmentsRoute) {
                Rectangle rectangleSegment = new Rectangle(unSegment.getXHautGauche(), unSegment.getYHautGauche(), DonneesGraphiques.largeurRectangle, DonneesGraphiques.hauteurRectangle);
                rectangleSegment.setId(nomRoute);
                rectangleSegment.setRotate(unSegment.getAngle());
                rectangleSegment.setFill(Color.TRANSPARENT);
                getChildren().add(rectangleSegment);



                // modif ici pour le choix de la route
                rectangleSegment.setOnMouseClicked(event -> {
                    System.out.println("On a cliqué sur une route");
                    ((VueDuJeu) getScene().getRoot()).getJeu().uneRouteAEteChoisie(route.getNom());
                });
                bindRectangle(rectangleSegment, unSegment.getXHautGauche(), unSegment.getYHautGauche());
            }

        }
    }

    public void routesPriseChangementCouleur(IRoute route, IJoueur joueur) {
        System.out.println(route.proprietaireProperty().get());


        ArrayList<DonneesGraphiques.DonneesSegments> segmentsRoute = DonneesGraphiques.routes.get(route.getNom());
        for (DonneesGraphiques.DonneesSegments unSegment : segmentsRoute) {
                Rectangle rectangleSegment = new Rectangle(unSegment.getXHautGauche(), unSegment.getYHautGauche(), DonneesGraphiques.largeurRectangle, DonneesGraphiques.hauteurRectangle);
                rectangleSegment.setId(route.getNom());
                rectangleSegment.setRotate(unSegment.getAngle());

                rectangleSegment.setFill(Color.web(mapCouleur(String.valueOf(joueur.getCouleur()))));
                rectangleSegment.setStroke(Color.BLACK);
                getChildren().add(rectangleSegment);
                bindRectangle(rectangleSegment, unSegment.getXHautGauche(), unSegment.getYHautGauche());
        }
    }

    public Color convertStringToColor(String color) {
        return Color.web(color);
    }

    private void bindRedimensionEtCentragePlateau() {
        mapMonde.fitWidthProperty().bind(widthProperty());
        mapMonde.fitHeightProperty().bind(heightProperty());
        mapMonde.layoutXProperty().bind(new DoubleBinding() { // Pour maintenir le plateau au centre
            {
                super.bind(widthProperty(),heightProperty());
            }
            @Override
            protected double computeValue() {
                double imageViewWidth = mapMonde.getLayoutBounds().getWidth();
                return (getWidth() - imageViewWidth) / 2;
            }
        });
    }

    private void bindCerclePortAuPlateau(DonneesGraphiques.DonneesCerclesPorts port, Circle cerclePort) {
        cerclePort.centerXProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutX() + port.centreX() * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        cerclePort.centerYProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutY() + port.centreY() * mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
        cerclePort.radiusProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return DonneesGraphiques.rayonInitial * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
    }

    private void bindRectangle(Rectangle rect, double layoutX, double layoutY) {
        rect.widthProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return DonneesGraphiques.largeurRectangle * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.heightProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return DonneesGraphiques.hauteurRectangle * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.layoutXProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutX() + layoutX * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.xProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());}
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;

            }
        });
        rect.layoutYProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return layoutY * mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
        rect.yProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
    }
    private void ajouterVilles() {
        for (String nomVille : DonneesGraphiques.villes.keySet()) {
            DonneesGraphiques.DonneesCerclesPorts positionVilleSurPlateau = DonneesGraphiques.villes.get(nomVille);
            Circle cercleVille = new Circle(positionVilleSurPlateau.centreX(), positionVilleSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
            cercleVille.setId(nomVille);
            getChildren().add(cercleVille);
            bindCerclePortAuPlateau(positionVilleSurPlateau, cercleVille);
            cercleVille.setOnMouseClicked(choixPort);
        }
    }
}
