package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {

    @FXML
    private Label headLabel;

    @FXML
    private ImageView imgBg;

    @FXML
    private AnchorPane mainAnc;

    @FXML
    void ClickOnLabel(MouseEvent event) {
        NavigateTo("/view/LoginPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //NavigateTo("/view/StartPage.fxml");
    }

    private void NavigateTo(String fxmlPath) {
        try{
            mainAnc.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainAnc.getChildren().add(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load page...").show();
        }
    }
}
