package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.*;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import fr.umontpellier.iut.rails.mecanique.Route;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;

import java.util.ArrayList;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends VBox {


    private final IJeu jeu;
    private VuePlateau plateau;

    private  ArrayList<IJoueur> listeJoueurs = new ArrayList<>();

    HBox listeDestination = new HBox();

    HBox hbox_passer_score = new HBox();

    VueJoueurCourant joueurCourant;



    public VueDuJeu(IJeu jeu) {





        this.jeu = jeu;




        plateau = new VuePlateau();
        plateau.setMinSize(1250, 600);
        plateau.setMaxSize(1250, 600);
        listeDestination.setSpacing(10);

        this.prefWidthProperty().bind(plateau.widthProperty());
        this.prefHeightProperty().bind(plateau.heightProperty());

        Label texte_du_jeu = new Label();
        texte_du_jeu.textProperty().bind(jeu.instructionProperty());
        texte_du_jeu.setStyle("-fx-font-family: 'IM FELL English SC';" +
        "-fx-font-size:" + Screen.getPrimary().getBounds().getWidth()/50 + ";" +
                "-fx-font-weight: bold;"+ "-fx-text-fill: #000000;");
        texte_du_jeu.setPadding(new Insets(10));

        HBox hbox_texte_du_haut = new HBox();
        hbox_texte_du_haut.setAlignment(Pos.BASELINE_LEFT);
        hbox_texte_du_haut.getChildren().add(texte_du_jeu);





        HBox jeu_plateau = new HBox();

        jeu_plateau.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                hbox_texte_du_haut.setPrefWidth(newSceneWidth.doubleValue());
            }
        });
        jeu_plateau.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                hbox_texte_du_haut.setPrefHeight(newSceneHeight.doubleValue());
            }
        });

        // Vue du joueur actuelle
        joueurCourant = new VueJoueurCourant(jeu);
        joueurCourant.creerBindings(jeu);
        Button passer = new Button("Passer");
        passer.setStyle("-fx-background-color: #3baae8; -fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 20px; -fx-text-fill: black;");
        passer.setAlignment(Pos.CENTER);



        hbox_passer_score.widthProperty().addListener(
                (observable, oldValue, newValue) -> {
                    passer.setPrefWidth((double) newValue * 0.65);
                });
        hbox_passer_score.heightProperty().addListener(
                (observable, oldValue, newValue) -> {
                    passer.setPrefHeight((double) newValue);
                });

        passer.setOnMouseClicked(
                event -> {
                    jeu.passerAEteChoisi();

                });
        passer.setOnKeyPressed(
                event -> {
                    System.out.println("entrer");
                    if (event.getCode() == KeyCode.ENTER) {
                        jeu.passerAEteChoisi();
                    }
                });

        passer.setOnMouseEntered(
                event -> {
                    passer.setScaleX(1.05);
                    passer.setScaleY(1.05);
                });
        passer.setOnMouseExited(
                event -> {
                    passer.setScaleX(1);
                    passer.setScaleY(1);
                });
        hbox_passer_score.setPadding(new Insets(10));
        hbox_passer_score.setSpacing(10);
        hbox_passer_score.getChildren().add(passer);

        Button btn_score = new Button("Score");
        btn_score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 20px; -fx-text-fill: black;" +
                "-fx-font-family: 'IM FELL English SC';");
        btn_score.setAlignment(Pos.CENTER);
        btn_score.setOnMouseClicked(
                event -> {
                    System.out.println("ajouter score");
                    VueResultats vueResultats = new VueResultats(jeu);
                    jeu_plateau.heightProperty().addListener(
                            (observable, oldValue, newValue) -> {
                                vueResultats.setPrefHeight( 0.5 * (double) newValue);
                            });
                    jeu_plateau.getChildren().add(vueResultats);
                    btn_score.setOnMouseClicked(
                            event1 -> {
                                if (!jeu_plateau.getChildren().contains(vueResultats)) {
                                    jeu_plateau.getChildren().add(vueResultats);
                                }
                                else {
                                    jeu_plateau.getChildren().remove(vueResultats);
                                }
                            });
                });

        hbox_passer_score.getChildren().add(btn_score);
        joueurCourant.getChildren().addAll(hbox_passer_score);

        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                joueurCourant.setPrefWidth(newSceneWidth.doubleValue() - 2000);
            }
        });
        this.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                joueurCourant.setPrefHeight(newSceneHeight.doubleValue());
            }
        });

        //plateau.setBorder(Border.stroke(Paint.valueOf("#000000")));
        jeu_plateau.getChildren().addAll(plateau, joueurCourant);

        VBox deroulement_jeu = new VBox();

        HBox hBox_btn_passer = new HBox();


        VBox vBox_texte_du_jeu = new VBox();
        vBox_texte_du_jeu.getChildren().addAll(listeDestination);

        //vBox_texte_du_jeu.setBorder(Border.stroke(Paint.valueOf("#000000")));

        vBox_texte_du_jeu.setPrefSize(0.01 * this.getWidth(), 0.01 * this.getHeight());

        deroulement_jeu.getChildren().addAll(hBox_btn_passer, vBox_texte_du_jeu);

        jeu.destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                listeDestination.getChildren().clear();
            for (IDestination destination : jeu.destinationsInitialesProperty()) {
                    VueDestination vueDestination = new VueDestination(destination, jeu);
                    vueDestination.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            jeu.uneDestinationAEteChoisie(vueDestination.getDestination());
                        }
                    });
                    listeDestination.getChildren().addAll(vueDestination.getListeDestination());
                }
            }
        });

        HBox listeCartes = new HBox();

        listeCartes.setStyle(" -fx-border-color: red; -fx-border-width: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        listeCartes.setMaxWidth(Screen.getPrimary().getBounds().getWidth() / 3);


        for (ICarteTransport carteTransport : jeu.cartesTransportVisiblesProperty()) {
            VueCarteTransport vueCarteTransport = new VueCarteTransport(carteTransport, 1, jeu, 0.12 * Screen.getPrimary().getBounds().getHeight() / 3);
            vueCarteTransport.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    jeu.uneCarteTransportAEteChoisie(vueCarteTransport.getCarteTransport());
                }
            });
            listeCartes.getChildren().add(vueCarteTransport);
        }
        jeu.cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {

            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                listeCartes.getChildren().clear();
                for (ICarteTransport carteTransport : jeu.cartesTransportVisiblesProperty()) {
                    VueCarteTransport vueCarteTransport = new VueCarteTransport(carteTransport, 1, jeu,  0.12 * Screen.getPrimary().getBounds().getHeight() / 3);
                    vueCarteTransport.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            jeu.uneCarteTransportAEteChoisie(vueCarteTransport.getCarteTransport());
                        }
                    });
                    listeCartes.getChildren().add(vueCarteTransport);
                }
            }
        });

        jeu.saisieNbPionsWagonAutoriseeProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                if (t1) {

                    //jeu.saisieNbPionsBateauAutoriseeProperty().setValue(true);
                    Button boutonValider = new Button("Valider");
                    boutonValider.setDisable(true);
                    TextField textField = new TextField();
                    textField.setPromptText("Nombre de pions autorisés");
                    textField.setPrefSize(200, 50);
                    textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");
                    textField.setPadding(new Insets(10));
                    textField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                            try {
                                boutonValider.setDisable(false);
                            } catch (NumberFormatException e) {
                                boutonValider.setDisable(true);
                            }
                        }
                    });
                    boutonValider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            jeu.leNombreDePionsSouhaiteAEteRenseigne(textField.getText());
                        }
                    });
                    textField.setOnKeyPressed(
                            event -> {
                                if (event.getCode() == KeyCode.ENTER) {
                                    jeu.leNombreDePionsSouhaiteAEteRenseigne(textField.getText());
                                }
                            });
                    hBox_btn_passer.getChildren().addAll(textField, boutonValider);
                } else {
                    hBox_btn_passer.getChildren().clear();
                    //hBox_btn_passer.getChildren().add(passer);
                }
            }
        });
        //jeu.destinationsInitialesProperty().addListener(destinationListChangeListener);
        deroulement_jeu.setPadding(new javafx.geometry.Insets(20,20,20,20));
        VBox vbox_jeuPlateauCartesTransportvisibles = new VBox();


        /*joueurCourant.heightProperty().addListener((observable, oldValue, newValue) -> {
           jeu_plateau.setSpacing(- 0.1 * newValue.doubleValue());
        });*/

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            plateau.setMinHeight((double) newValue * 0.5);
            plateau.setMaxHeight((double) newValue * 0.5);
            listeDestination.setMinHeight((double) newValue * 0.5);
            listeDestination.setMaxHeight((double) newValue * 0.5);
        });
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            plateau.setMinWidth((double) newValue * 0.5);
            plateau.setMaxWidth((double) newValue * 0.5);
            listeDestination.setMinWidth((double) newValue * 0.5);
            listeDestination.setMaxWidth((double) newValue * 0.5);
        });


        vbox_jeuPlateauCartesTransportvisibles.getChildren().addAll(jeu_plateau, listeCartes);

        vbox_jeuPlateauCartesTransportvisibles.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        vbox_jeuPlateauCartesTransportvisibles.setAlignment(Pos.CENTER);
        getChildren().addAll(hbox_texte_du_haut, vbox_jeuPlateauCartesTransportvisibles, deroulement_jeu);
    }





    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
            joueurCourant.setMaxSize(0.1 * newValue.doubleValue(), 0.1 * getScene().heightProperty().doubleValue());
        });
        plateau.creerBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }
}
