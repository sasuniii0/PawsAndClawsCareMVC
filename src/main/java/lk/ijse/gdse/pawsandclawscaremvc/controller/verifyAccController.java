package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class verifyAccController {

    @FXML
    private AnchorPane pane;

    @FXML
    private Button BtnVerify;

    @FXML
    private Hyperlink HypeChange;

    @FXML
    private Label ShowLblId;

    @FXML
    private AnchorPane SignInPane;

    @FXML
    private TextField TxtCode;

    private static String code = null;
    private static String selectedEmail = "";

    public static void setUserData(String verificationCode, String email) {
        code = verificationCode;
        selectedEmail = email;
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("/view/" + location + ".fxml")))); // Fixed path
        stage.centerOnScreen();
    }

    @FXML
    void BtnVerifyOnAction(ActionEvent event) throws IOException {
        if (String.valueOf(code).equals(TxtCode.getText().trim())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ResetPwd.fxml"));
            Parent parent = fxmlLoader.load();

            // Fetch and set data to ResetPasswordController
            ResetPwdController controller = fxmlLoader.getController();
            controller.setUserData(selectedEmail);

            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.centerOnScreen();
        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong Verification Code").show();
        }
    }

    @FXML
    void HypeChangeOnAction(ActionEvent event) throws IOException {
        setUi("ForgotPwd");
    }

    @FXML
    void TxtCodeOnAction(ActionEvent event) {
    }

}
