package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.pawsandclawscaremvc.model.UserModel;
import java.io.IOException;

public class ResetPwdController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button BtnChange;

    @FXML
    private Label ShowLblId;

    @FXML
    private AnchorPane SignInPane;

    @FXML
    private TextField TxtNewPwd;

    private String selectedEmail = "";

    public void setUserData(String email) {
        this.selectedEmail = email;
        System.out.println("Selected email: " + selectedEmail);
    }

    @FXML
    void BtnChangePnAction(ActionEvent event) throws IOException {
        String password = TxtNewPwd.getText();
        boolean isUpdate = UserModel.updateUser(selectedEmail, password);
        if (isUpdate) {
            new Alert(Alert.AlertType.INFORMATION, "Password updated successfully").show();
            Parent load = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
            root.getChildren().clear();
            root.getChildren().add(load);
        }else  {
            new Alert(Alert.AlertType.ERROR, "Password not updated").show();
        }

    }

}
