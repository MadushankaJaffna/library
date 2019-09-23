package controller;


import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ImageView member;
    public ImageView issue;
    public ImageView returns;
    public ImageView manage;
    public Label mainmenu;
    public Label description;
    public AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000),root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(2.0);
        fadeIn.play();

    }


    public void img_mouseEnterence(MouseEvent mouseEvent) {
        ImageView icon = (ImageView)mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), icon);
        scaleT.setToX(1.2D);
        scaleT.setToY(1.2D);
        scaleT.play();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);

        String cat =icon.getId();
        switch (cat){
            case "member":
                this.mainmenu.setText("Manage Members");
                this.description.setText("Click to add, update or delete Member");
                break;

            case "issue":
               this.mainmenu.setText("Issue Book");
                this.description.setText("Click to add, update or delete Issues");
                break;

            case "returns":
               this.mainmenu.setText("Handle Returns");
                this.description.setText("Click to Reserve, edit, delete Returns");
                break;

            case "manage":
               this.mainmenu.setText("Manage Book");
                this.description.setText("Click to Manage Books Details Or Search Details");
                    break;
        }

    }

    public void img_mouseExit(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D),icon);
        scaleT.setToX(1D);
        scaleT.setToY(1D);
        scaleT.play();
        icon.setEffect(null);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);
        mainmenu.setText("");
        description.setText("");
    }

    public void image_mouseclick(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            Parent root = null;
            ImageView imageView = (ImageView) mouseEvent.getSource();
            String cat = imageView.getId();

            switch (cat) {
                case "member":
                    root = FXMLLoader.load(this.getClass().getResource("../fxml/ManageMember.fxml"));
                    break;

                case "issue":
                    root = FXMLLoader.load(this.getClass().getResource("../fxml/IssueBooks.fxml"));
                    break;

                case "returns":
                    root = FXMLLoader.load(this.getClass().getResource("../fxml/reservebook.fxml"));
                    break;

                case "manage":
                    root = FXMLLoader.load(this.getClass().getResource("../fxml/ManageBooks.fxml"));
                    break;
            }
            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();


            }
        }
    }
}
