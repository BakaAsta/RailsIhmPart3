package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.mecanique.Route;
import fr.umontpellier.iut.rails.mecanique.data.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {


    private Label nomJoueur = new Label();

    private Label nbPionsWagonsLabel = new Label();
    private Label nbPionsBateauLabel = new Label();
    private Label nbPortsLabel = new Label();

    private Label scoreLabel = new Label();




    private ArrayList<CarteTransport> cartesTransport = new ArrayList<>();

    private ArrayList<Destination> carteDestinationJoueurCourant = new ArrayList<>();


    VBox vBox_destination_cache = new VBox();

    HBox hbox_vueAutresJoueurs = new HBox();


    private int nbPionsWagons;

    private int nbPionsBateau;
    private int nbPionsReserve;

    private GridPane carteTransportJoueurCourant = new GridPane();

    VBox vBox_hboxNomJoueur_CartesJoueurs_Destinations = new VBox();

    HBox hbox_destinationJoueurCourant = new HBox();

    HBox hbox_piohesCartes = new HBox();

    VBox vbox_destination_logo_piocheReplace_CartesVisible = new VBox();
    HBox hbox_destination_logo_piocheReplace = new HBox();


    // modification on passe le Gridpane sous forme de Hbox avec 3 vbox dedans
// et juste comme je trie la liste dans un certain ordre j'ai pu qu'a ajouter comme je le souhaite
    HBox hbox_carteTransportJoueurCourantModifGridpane = new HBox();

    VBox vbox_carteTransportWagons = new VBox();
    VBox vbox_carteTransportBateaux = new VBox();
    VBox vbox_carteTransportBateauDlbEtJoker = new VBox();

    HBox hbox_listesCartesTransportsVisibles = new HBox();


    private final IJeu jeu;


    // Dans la vue du Joueur courant doit etre présent :
    // - le nom du joueur
    // - le score du joueur a coté du nom en haut a droite
    // - le nombre de pions wagons avec le logo
    // - le nombre de pions bateaux avec le logo
    // - le nombre de pions de réserve si jamais
    // - les cartes destinations du joueur
    // - les cartes transport du joueur sous forme de gridPane ordonnées de la manière suivante :
    // - les carte en double ne sont pas afficher mais un nombre indique le nombre de carte en double au dessus de la carte en question
    // - les cartes sont afffiche, tous a gauche que les carte wagons au millieu les cartes bateaux et a droite les cartes joker et bateau double
    // Les pioches disponibles, wagons, bateau, destinations
    // La liste des joueurs en attente en dessous de tte les pioches
    // un bouton passer et un bouton score pour regarder la table des score


    public VueJoueurCourant(IJeu jeu) {
        this.jeu = jeu;


        vBox_hboxNomJoueur_CartesJoueurs_Destinations.setPadding(new Insets(10));
        vBox_hboxNomJoueur_CartesJoueurs_Destinations.setSpacing(10);

        if (jeu.joueurCourantProperty().get() != null) {
            vBox_hboxNomJoueur_CartesJoueurs_Destinations.setStyle("-fx-background-color:" + jeu.joueurCourantProperty().get().getCouleur() +  ";" + "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        }

        this.setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 5px;");

        this.setSpacing(10);
        HBox nomJoueurHBox = new HBox();

        nomJoueurHBox.setBorder(Border.stroke(javafx.scene.paint.Color.BLACK));

        scoreLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        nomJoueurHBox.getChildren().add(nomJoueur);
        nomJoueurHBox.getChildren().add(scoreLabel);

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            nomJoueurHBox.setSpacing(newValue.doubleValue() - 0.5 *nomJoueurHBox.getWidth());
            nomJoueurHBox.setBorder(Border.stroke(Color.PINK));
            hbox_vueAutresJoueurs.setPrefWidth(newValue.doubleValue());
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            hbox_vueAutresJoueurs.setPrefWidth(newValue.doubleValue());
        });


        nomJoueur.setStyle("-fx-font-family: 'IM FELL English SC';" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;");
        scoreLabel.setFont(new Font("IM FELL English SC", 20));


        this.getChildren().add(nomJoueurHBox);

        VBox vboxCarteJoueur = new VBox();

        carteTransportJoueurCourant = new GridPane();
        carteTransportJoueurCourant.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Stop[] stops = new Stop[] {
                new Stop(0, Color.DARKSLATEBLUE), new Stop(1, Color.DARKRED)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
        vboxCarteJoueur.setBorder(new Border(new BorderStroke(lg1, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(5))));
        vboxCarteJoueur.getChildren().add(carteTransportJoueurCourant);

        hbox_destinationJoueurCourant = new HBox();

        //hbox_carteTransportJoueurCourantModifGridpane.setMaxSize(100, 100);
        vbox_carteTransportWagons.setMaxSize(hbox_destinationJoueurCourant.getMaxWidth(), hbox_destinationJoueurCourant.getMaxHeight());
        vbox_carteTransportBateaux.setMaxSize(hbox_destinationJoueurCourant.getMaxWidth(), hbox_destinationJoueurCourant.getMaxHeight());
        vbox_carteTransportBateauDlbEtJoker.setMaxSize(hbox_destinationJoueurCourant.getMaxWidth(), hbox_destinationJoueurCourant.getMaxHeight());


        hbox_carteTransportJoueurCourantModifGridpane.getChildren().addAll(vbox_carteTransportWagons, vbox_carteTransportBateaux, vbox_carteTransportBateauDlbEtJoker);


        hbox_carteTransportJoueurCourantModifGridpane.setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        hbox_carteTransportJoueurCourantModifGridpane.setPadding(new Insets(10));
        hbox_carteTransportJoueurCourantModifGridpane.setSpacing(5);
        vBox_hboxNomJoueur_CartesJoueurs_Destinations.getChildren().addAll(nomJoueurHBox, hbox_carteTransportJoueurCourantModifGridpane);



        this.getChildren().add(vBox_hboxNomJoueur_CartesJoueurs_Destinations);

        HBox hBox_pionsJoueurCourant = new HBox();
        hBox_pionsJoueurCourant.setSpacing(10);
        hBox_pionsJoueurCourant.setPadding(new Insets(10));
        hBox_pionsJoueurCourant.setAlignment(Pos.CENTER);
        System.out.println(nbPionsWagonsLabel.getText());
        System.out.println(nbPionsBateauLabel.getText());
        System.out.println(nbPortsLabel.getText());
        hBox_pionsJoueurCourant.getChildren().addAll(nbPionsWagonsLabel, nbPionsBateauLabel, nbPortsLabel);
        hbox_destinationJoueurCourant.getChildren().addAll(vBox_destination_cache, hBox_pionsJoueurCourant);

        ImageView imagePiocheWagons = new ImageView(new Image("file:src/main/resources/images/cartesWagons/dos-WAGON.png"));
        imagePiocheWagons.setFitHeight(160 + 0.05 * Screen.getPrimary().getBounds().getHeight() / 3);
        imagePiocheWagons.setFitWidth(100 + 0.05 * Screen.getPrimary().getBounds().getWidth() / 3);

        imagePiocheWagons.setOnMouseClicked( event -> {
            jeu.uneCarteWagonAEtePiochee();
        });

        ImageView imagePiocheBateaux = new ImageView(new Image("file:src/main/resources/images/cartesWagons/dos-BATEAU.png"));
        imagePiocheBateaux.setFitHeight(160 + 0.05 * Screen.getPrimary().getBounds().getHeight() / 3);
        imagePiocheBateaux.setFitWidth(100 + 0.05 * Screen.getPrimary().getBounds().getWidth() / 3);

        imagePiocheBateaux.setOnMouseClicked( event -> {
            jeu.uneCarteBateauAEtePiochee();
        });


        ImageView imagePiocheDestination = new ImageView(new Image("file:src/main/resources/images/cartesWagons/destinations.png"));
        imagePiocheDestination.setFitHeight(100 + 0.05 * Screen.getPrimary().getBounds().getHeight() / 3);
        imagePiocheDestination.setFitWidth(160 + 0.05 * Screen.getPrimary().getBounds().getWidth() / 3);



        imagePiocheDestination.setOnMouseClicked( event -> {
            jeu.nouvelleDestinationDemandee();
            for (IDestination destination : jeu.destinationsInitialesProperty()) {
                VueDestination vueDestination = new VueDestination(destination, jeu);
                HBox hBox_piocher_destination = new HBox();
                hBox_piocher_destination.getChildren().add(vueDestination);
            }
        });

        ImageView imageReplaceByWagons = new ImageView(new Image("file:src/main/resources/images/bouton-pions-wagon.png"));
        imageReplaceByWagons.setFitHeight(40 + 0.03 * Screen.getPrimary().getBounds().getHeight() / 3);
        imageReplaceByWagons.setFitWidth(25 + 0.03 * Screen.getPrimary().getBounds().getWidth() / 3);

        imageReplaceByWagons.setOnMouseClicked( event -> {
            jeu.uneCarteWagonAEtePiochee();
        });


        ImageView imageReplaceByBateaux = new ImageView(new Image("file:src/main/resources/images/bouton-pions-bateau.png"));
        imageReplaceByBateaux.setFitHeight(40 + 0.03 * Screen.getPrimary().getBounds().getHeight() / 3);
        imageReplaceByBateaux.setFitWidth(25 + 0.03 * Screen.getPrimary().getBounds().getWidth() / 3);

        imageReplaceByBateaux.setOnMouseClicked( event -> {
            jeu.uneCarteBateauAEtePiochee();
        });

        vbox_destination_logo_piocheReplace_CartesVisible.getChildren().addAll(hbox_destination_logo_piocheReplace, imagePiocheDestination);



        hbox_destination_logo_piocheReplace.setSpacing(10);
        hbox_destination_logo_piocheReplace.setAlignment(Pos.CENTER);
        hbox_destination_logo_piocheReplace.setPadding(new Insets(0, 0,5,0));
        hbox_destination_logo_piocheReplace.getChildren().addAll(imageReplaceByWagons, imageReplaceByBateaux);
        hbox_piohesCartes.setSpacing(10);
        hbox_piohesCartes.getChildren().addAll(imagePiocheWagons, imagePiocheBateaux, vbox_destination_logo_piocheReplace_CartesVisible);

        hbox_destinationJoueurCourant.setMaxSize(this.getMaxWidth(), this.getMaxHeight());


        this.getChildren().addAll(hbox_destinationJoueurCourant);
        hbox_piohesCartes.setPadding(new Insets(10));
        hbox_vueAutresJoueurs.setPadding(new Insets(10));
        hbox_vueAutresJoueurs.setSpacing(10);


        this.getChildren().addAll(hbox_piohesCartes, hbox_vueAutresJoueurs);
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

    public void creerBindings(IJeu jeu) {

        // il faudrait arrivait a choper l'etat du jeu pour savoir si le joueurCourant tente de prendre une destination et si c le cas faire un event sur les carte de sont jeu sinon non




        jeu.joueurCourantProperty().addListener((observable, oldValue, newValue) -> {


            VueAutresJoueurs vueAutresJoueurs = new VueAutresJoueurs(jeu, newValue);

            int tailleElement = this.getChildren().stream().mapToInt(node -> (int) node.getBoundsInParent().getWidth()).sum();

            vueAutresJoueurs.setSpacing(250 - tailleElement);

            hbox_vueAutresJoueurs.getChildren().clear();
            hbox_vueAutresJoueurs.setAlignment(Pos.CENTER);
            hbox_vueAutresJoueurs.getChildren().add(vueAutresJoueurs);


            VueAutresJoueurs vueAutresJoueurstqt = new VueAutresJoueurs(jeu, newValue);
            vueAutresJoueurstqt.setSpacing(250 - tailleElement);

            jeu.joueurCourantProperty().addListener((observable1, oldValue1, newValue1) -> {
                if (newValue1 != null) {

                    VueAutresJoueurs vueAutresJoueurs2 = new VueAutresJoueurs(jeu, newValue1);

                    vueAutresJoueurs2.setSpacing(100);

                    hbox_vueAutresJoueurs.getChildren().clear();
                    hbox_vueAutresJoueurs.setAlignment(Pos.CENTER);
                    hbox_vueAutresJoueurs.getChildren().add(vueAutresJoueurs2);
                }
            });


            Image imageWagon = new Image("file:src/main/resources/images/bouton-pions-wagon.png");
            ImageView imageViewWagon = new ImageView(imageWagon);
            imageViewWagon.setFitHeight(35);
            imageViewWagon.setFitWidth(35);
            nbPionsWagonsLabel.setText(String.valueOf(newValue.nbPionsWagonsProperty().getValue()));
            nbPionsWagonsLabel.setGraphic(imageViewWagon);

            nbPionsWagonsLabel.setOnMouseClicked( event -> {
                System.out.println("demande de wagons");
                jeu.nouveauxPionsWagonsDemandes();
            });



            Image imageBateau = new Image("file:src/main/resources/images/bouton-pions-bateau.png");
            ImageView imageViewBateau = new ImageView(imageBateau);
            imageViewBateau.setFitHeight(35);
            imageViewBateau.setFitWidth(35);
            nbPionsBateauLabel.setText(String.valueOf(newValue.nbPionsBateauxProperty().getValue()));
            nbPionsBateauLabel.setGraphic(imageViewBateau);

            nbPionsBateauLabel.setOnMouseClicked( event -> {
                System.out.println("demande de bateaux");
                jeu.nouveauxPionsBateauxDemandes();
            });


            Image imagePort = new Image("file:src/main/resources/images/port.png");
            ImageView imageViewPort = new ImageView(imagePort);
            imageViewPort.setFitHeight(35);
            imageViewPort.setFitWidth(35);
            nbPortsLabel.setText(String.valueOf( 3 - newValue.getNbPorts()));
            nbPortsLabel.setGraphic(imageViewPort);




            Image image = new Image("file:src/main/resources/images/cartesWagons/" + "avatar-" + newValue.getCouleur() + ".png");
            ImageView imageViewJoueur = new ImageView(image);
            imageViewJoueur.setFitHeight(50);
            imageViewJoueur.setFitWidth(50);

            // a modifier pour faire un dégradé de couleur
            vBox_hboxNomJoueur_CartesJoueurs_Destinations.setStyle("-fx-background-color:" + (mapCouleur(String.valueOf(jeu.joueurCourantProperty().get().getCouleur())) + ";" + "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));


            nomJoueur.setText(newValue.getNom());
            nomJoueur.setGraphic(imageViewJoueur);

            scoreLabel.setText("Score : " + newValue.getScore());


            cartesTransport = new ArrayList<>();
            cartesTransport.addAll((Collection<? extends CarteTransport>) newValue.getCartesTransport());


            for (CarteTransport carteTransport: cartesTransport) {
                System.out.println(carteTransport);
            }

            cartesTransport.sort(Comparator.comparing(CarteTransport::getCouleur)
                    .thenComparing(CarteTransport::getType)
                    .thenComparing(CarteTransport::estDouble));



            System.out.println(cartesTransport.size());

            for (CarteTransport carteTransport: cartesTransport) {
                System.out.println(carteTransport);
            }

            vbox_carteTransportWagons.getChildren().clear();
            vbox_carteTransportBateaux.getChildren().clear();
            vbox_carteTransportBateauDlbEtJoker.getChildren().clear();

            for (CarteTransport carteTransport: cartesTransport) {
                boolean cartePresente = false;
                VueCarteTransport cartePane = new VueCarteTransport(carteTransport, cartesTransport.stream().filter(c -> c.equals(carteTransport)).toArray().length, jeu,  0.05 * Screen.getPrimary().getBounds().getHeight() / 3);

                if (carteTransport.getType().equals(TypeCarteTransport.WAGON)) {
                    for (Node element : vbox_carteTransportWagons.getChildren()) {
                        if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                            cartePresente = true;
                            break;
                        }
                    }
                    if (cartePresente) {
                        System.out.println("la carte est déja présente dans les cartes du joueurs");
                    }
                    else {
                        for (Node element : vbox_carteTransportWagons.getChildren()) {
                            if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {

                            }
                        }
                        vbox_carteTransportWagons.getChildren().add(cartePane);
                    }
                }
                else if (carteTransport.getType().equals(TypeCarteTransport.BATEAU) && !carteTransport.estDouble()) {
                    for (Node element : vbox_carteTransportBateaux.getChildren()) {
                        if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                            cartePresente = true;
                            break;
                        }
                    }
                    if (cartePresente) {
                        System.out.println("la carte est déja présente dans les cartes du joueurs");
                    }
                    else {
                        vbox_carteTransportBateaux.getChildren().add(cartePane);
                    }
                }
                else if (carteTransport.getType().equals(TypeCarteTransport.JOKER)) {
                    for (Node element : vbox_carteTransportBateauDlbEtJoker.getChildren()) {
                        if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                            cartePresente = true;
                            break;
                        }
                    }
                    if (cartePresente) {
                        System.out.println("la carte est déja présente dans les cartes du joueurs");
                    }
                    else {
                        vbox_carteTransportBateauDlbEtJoker.getChildren().add(cartePane);
                    }
                }
                else {
                    for (Node element : vbox_carteTransportBateauDlbEtJoker.getChildren()) {
                        if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                            cartePresente = true;
                            break;
                        }
                    }
                    if (cartePresente) {
                        System.out.println("la carte est déja présente dans les cartes du joueurs");
                    }
                    else {
                        vbox_carteTransportBateauDlbEtJoker.getChildren().add(cartePane);
                    }
                }
                if (cartePresente) {
                    continue;
                }
            }

            jeu.joueurCourantProperty().get().cartesTransportProperty().addListener(new ListChangeListener<ICarteTransport>() {
                @Override
                public void onChanged(Change<? extends ICarteTransport> change) {
                    cartesTransport = new ArrayList<>();
                    cartesTransport.addAll((Collection<? extends CarteTransport>) newValue.getCartesTransport());


                    for (CarteTransport carteTransport: cartesTransport) {
                        System.out.println(carteTransport);
                    }

                    cartesTransport.sort(Comparator.comparing(CarteTransport::getCouleur)
                            .thenComparing(CarteTransport::getType)
                            .thenComparing(CarteTransport::estDouble));



                    System.out.println(cartesTransport.size());

                    for (CarteTransport carteTransport: cartesTransport) {
                        System.out.println(carteTransport);
                    }


                    vbox_carteTransportWagons.getChildren().clear();
                    vbox_carteTransportBateaux.getChildren().clear();
                    vbox_carteTransportBateauDlbEtJoker.getChildren().clear();

                    for (CarteTransport carteTransport: cartesTransport) {
                        boolean cartePresente = false;
                        VueCarteTransport cartePane = new VueCarteTransport(carteTransport, cartesTransport.stream().filter(c -> c.equals(carteTransport)).toArray().length, jeu , 0.05 * Screen.getPrimary().getBounds().getHeight() / 3);

                        if (carteTransport.getType().equals(TypeCarteTransport.WAGON)) {
                            for (Node element : vbox_carteTransportWagons.getChildren()) {
                                if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                                    cartePresente = true;
                                    break;
                                }
                            }
                            if (cartePresente) {
                                System.out.println("la carte est déja présente dans les cartes du joueurs");
                            }
                            else {
                                for (Node element : vbox_carteTransportWagons.getChildren()) {
                                    if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {

                                    }
                                }
                                vbox_carteTransportWagons.getChildren().add(cartePane);
                            }
                        }
                        else if (carteTransport.getType().equals(TypeCarteTransport.BATEAU) && !carteTransport.estDouble()) {
                            for (Node element : vbox_carteTransportBateaux.getChildren()) {
                                if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                                    cartePresente = true;
                                    break;
                                }
                            }
                            if (cartePresente) {
                                System.out.println("la carte est déja présente dans les cartes du joueurs");
                            }
                            else {
                                vbox_carteTransportBateaux.getChildren().add(cartePane);
                            }
                        }
                        else if (carteTransport.getType().equals(TypeCarteTransport.JOKER)) {
                            for (Node element : vbox_carteTransportBateauDlbEtJoker.getChildren()) {
                                if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                                    cartePresente = true;
                                    break;
                                }
                            }
                            if (cartePresente) {
                                System.out.println("la carte est déja présente dans les cartes du joueurs");
                            }
                            else {
                                vbox_carteTransportBateauDlbEtJoker.getChildren().add(cartePane);
                            }
                        }
                        else {
                            for (Node element : vbox_carteTransportBateauDlbEtJoker.getChildren()) {
                                if (((VueCarteTransport) element).getCarteTransport().equals(cartePane.getCarteTransport())) {
                                    cartePresente = true;
                                    break;
                                }
                            }
                            if (cartePresente) {
                                System.out.println("la carte est déja présente dans les cartes du joueurs");
                            }
                            else {
                                vbox_carteTransportBateauDlbEtJoker.getChildren().add(cartePane);
                            }
                        }
                        if (cartePresente) {
                            continue;
                        }
                    }
                }
            });

            carteDestinationJoueurCourant = new ArrayList<>();
            carteDestinationJoueurCourant.addAll((Collection<? extends Destination>) newValue.getDestinations());

            Label destination_cache = new Label("DESTINATION");
            //destination_cache.setBorder(Border.stroke(Color.BLACK));
            destination_cache.setStyle("-fx-font-family: 'IM FELL English SC';" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;"+
                    "-fx-padding: 0 "+0.05 * Screen.getPrimary().getBounds().getHeight() / 3+ " 0 "+0.05 * Screen.getPrimary().getBounds().getHeight() / 3+ ";");

            vBox_destination_cache.getChildren().clear();
            vBox_destination_cache.getChildren().add(destination_cache);
            //vBox_destination_cache.setBorder(Border.stroke(Color.DARKRED));
            vBox_destination_cache.setSpacing(10);
            vBox_destination_cache.setPadding(new Insets(10));

            destination_cache.setOnScroll( event -> {
                if (event.getDeltaY() < 0) {
                    vBox_destination_cache.getChildren().clear();
                    destination_cache.setText("Liste de vos destinations :");
                    destination_cache.setStyle("-fx-font-family: 'Dancing Script';" +
                            "-fx-font-size: 20px;");
                    vBox_destination_cache.getChildren().add(destination_cache);
                    for (Destination destination : carteDestinationJoueurCourant) {

                        Label destinationLabel = new Label(destination.getVilles().get(0) + " - " + destination.getVilles().get(destination.getVilles().size() - 1));
                        destinationLabel.setPadding(new Insets(10));
                        destinationLabel.setStyle("-fx-font-family: 'IM FELL English SC';" +
                                "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-text-fill: black;" + "-fx-border-color: black;" + "-fx-border-width: 3px; -fx-border-radius: 5px;");
                        destinationLabel.setOnMouseEntered(
                                event1 -> {

                                    for (Node element : vBox_destination_cache.getChildren()) {
                                        if (element.equals(destinationLabel)) {
                                            Label destinatonLabelcomplet = new Label();
                                            for (String ville : destination.getVilles()) {
                                                destinatonLabelcomplet.setText(destinatonLabelcomplet.getText() + "  " + ville + "\n");
                                            }
                                            destinatonLabelcomplet.setText(destinatonLabelcomplet.getText() + "  " + destination.getValeur() + " points");
                                            destinationLabel.setText(destinatonLabelcomplet.getText());
                                            destinationLabel.setMaxSize(200, 200);
                                            Stop[] stops = new Stop[] {
                                                    new Stop(0, Color.DARKSLATEBLUE), new Stop(1, Color.DARKRED)};
                                            LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
                                            destinationLabel.setBorder(new Border(new BorderStroke(lg1, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(5))));
                                        }
                                    }
                                }
                        );
                        destinationLabel.setOnMouseExited(
                                event1 -> {
                                    for (Node element : vBox_destination_cache.getChildren()) {
                                        if (element.equals(destinationLabel)) {
                                            destinationLabel.setText(destination.getVilles().get(0) + " - " + destination.getVilles().get(destination.getVilles().size() - 1));
                                            destinationLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3))));
                                            destinationLabel.setStyle("-fx-font-family: 'IM FELL English SC';" +
                                                    "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-text-fill: black;" + "-fx-border-color: black;" + "-fx-border-width: 3px; -fx-border-radius: 5px;");
                                            break;
                                        }
                                    }
                                }
                        );
                        vBox_destination_cache.getChildren().add(destinationLabel);
                    }
                }
                else if (event.getDeltaY() > 0) {
                    vBox_destination_cache.getChildren().clear();
                    destination_cache.setText("DESTINATION");
                    destination_cache.setStyle("-fx-font-family: 'IM FELL English SC';" +
                            "-fx-font-size: 20px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 0 150 0 0 ;");
                    vBox_destination_cache.getChildren().add(destination_cache);
                }
            });
        });
    }
}
