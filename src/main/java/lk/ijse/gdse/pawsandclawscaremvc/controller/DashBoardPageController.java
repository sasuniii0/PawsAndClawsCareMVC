package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardPageController implements Initializable {

    @FXML
    private AnchorPane ancPaneLoad;

    @FXML
    private AnchorPane root;

    @FXML
    private Label LblDash;

    @FXML
    private Button BtnPaymentManage;

    @FXML
    private Button btnCustomerManage;

    @FXML
    private Button btnEmpManage;

    @FXML
    private Button btnFeedback;

    @FXML
    private Button btnGenerateRep;

    @FXML
    private Button btnInvenManage;

    @FXML
    private Button btnOrderManage;

    @FXML
    private Button btnPetManage;

    @FXML
    private Button btnProductManage;

    @FXML
    private Button btnReservation;

    @FXML
    private Button btnServices;

    @FXML
    private Button btnSupManage;

    @FXML
    private Pane dashboardLoadPane;
    
    @FXML
    private Button BtnLogOut;

    @FXML
    void customerManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/CustomerManage.fxml");
    }

    private void navigateTo(String fxmlPath) throws IOException {
        try{
            ancPaneLoad.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(fxmlPath));
            ancPaneLoad.getChildren().add(anchorPane);
        }catch(IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load the page");
        }
    }

    @FXML
    void empManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/EmpManage.fxml");
    }

    @FXML
    void feedbackOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/Intro.fxml");
    }

    @FXML
    void generateRepOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/Payment.fxml");
    }

    @FXML
    void invenManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/InvenManage.fxml");
    }

    @FXML
    void orderManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/OrderManage.fxml");
    }

    @FXML
    void petManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/PetManage.fxml");
    }

    @FXML
    void productManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/Product.fxml");
    }

    @FXML
    void reservationOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/Reservation.fxml");
    }

    @FXML
    void serviceManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/Services.fxml");
    }

    @FXML
    void supManageOnClickAction(ActionEvent event) throws IOException {
        navigateTo("/view/SupManage.fxml");
    }

    public void BtnLogOutOnClickAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"))));
            stage.centerOnScreen();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            navigateTo("/view/Intro.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void BtnPaymentManageOnAction(ActionEvent actionEvent) throws IOException {
        navigateTo("/view/Payment.fxml");
    }

    public void LblDashOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("/view/DashBoardPage.fxml"))));
        stage.centerOnScreen();
    }
}
