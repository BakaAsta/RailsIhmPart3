package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends Pane {

    private final IDestination destination;


    private HBox listeDestination = new HBox();

    private IJeu jeu;




    public VueDestination(IDestination destination, IJeu jeu) {
        this.destination = destination;

        Label label_destination_complete = new Label();
            for (String ville : destination.getVilles()) {
                label_destination_complete.setText(label_destination_complete.getText() + ville + "\n");
            }
            Label label_destination_depart = new Label( destination.getVilles().get(0) + "         " + destination.getValeur());


            Label label_destination_arrivee = new Label(destination.getVilles().get(destination.getVilles().size() - 1));

            System.out.println(label_destination_complete.getText());

            Button boutonDestination = new Button(label_destination_depart.getText() +"\n" + label_destination_arrivee.getText());
            boutonDestination.setLineSpacing(10);
            boutonDestination.setPrefSize(200, 100);
            boutonDestination.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");
            boutonDestination.setPadding(new javafx.geometry.Insets(10));

            boutonDestination.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    jeu.uneDestinationAEteChoisie(destination);
                }
            });

            boutonDestination.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    VBox vbox_affichage_destination = new VBox(label_destination_complete);
                    vbox_affichage_destination.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");
                    for (Node node : listeDestination.getChildren()) {
                        if (node instanceof Button) {
                            Button button = (Button) node;
                            if (button.getText().equals(boutonDestination.getText())) {
                                button.setText(label_destination_complete.getText());
                                button.setPrefSize(300, 200);
                            }
                        }
                    }
                }
            });
            boutonDestination.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("DragExited");
                    System.out.println(listeDestination.getChildren());
                    for (Node node : listeDestination.getChildren()) {
                        if (node instanceof Button) {
                            Button button = (Button) node;
                            if (button.getText().equals(boutonDestination.getText())) {
                                button.setText(label_destination_depart.getText() +"\n" + label_destination_arrivee.getText());
                                button.setPrefSize(200, 100);
                            }
                        }
                    }
                }
            });
            listeDestination.getChildren().add(boutonDestination);
        }

    public HBox getListeDestination() {
        return listeDestination;
    }


    public IDestination getDestination() {
        return destination;
    }

}
