package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.pawsandclawscaremvc.db.Database;
import lk.ijse.gdse.pawsandclawscaremvc.dto.UserDto;
import lk.ijse.gdse.pawsandclawscaremvc.model.UserModel;
import java.io.IOException;
import java.util.Optional;

public class LoginPageController {

    @FXML
    private Button BtnCreateAnAccount;

    @FXML
    private Button btnShowId;

    @FXML
    private AnchorPane SignInPane;

    @FXML
    private TextField pwdTxt;

    @FXML
    private AnchorPane signInAncPane;
        //ShowLblId.textProperty().bind(Bindings.concat(pwdTxt.getText()));

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usrNameTxt;

    @FXML
    private Label ShowLblId;

    @FXML
    void signInOnClickAction(ActionEvent event) throws IOException {
        String email = usrNameTxt.getText().toLowerCase().trim();
        String password = pwdTxt.getText().trim();

        // Validate user input
        if (email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Email and Password cannot be empty!").show();
            return;
        }
        if (!email.matches("[^@]+@[^.]+\\..+")) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format!").show();
            return;
        }
        boolean isAvailable = UserModel.searchUser(email, password);
        if (isAvailable) {
            signInAncPane.getChildren().clear();
            Parent load = FXMLLoader.load(getClass().getResource("/view/DashBoardPage.fxml"));
            signInAncPane.getChildren().add(load);
        }else {
            new Alert(Alert.AlertType.WARNING, String.format("User not found (%s)", email)).show();
        }
        // Authenticate user
        Optional<UserDto> selectedUser = Database.userTable.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst();

    }

    private void setUi(ActionEvent event, String location) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/" + location + ".fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(anchorPane));
        currentStage.show();
    }

    @FXML
    void PwdKeyTypeOnAction(KeyEvent event) {
    }

    public void BtnForgotPwdOnClickAction(ActionEvent actionEvent) throws IOException {
        setUi(actionEvent, "ForgotPwd");
    }

    public void BtnCreateAnAccountOnClick(ActionEvent actionEvent) throws IOException {
        setUi(actionEvent, "SignUpPage");
    }

}
