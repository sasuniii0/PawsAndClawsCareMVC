package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.pawsandclawscaremvc.db.DBConnection;
import lk.ijse.gdse.pawsandclawscaremvc.dto.UserDto;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class signUpController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button BtnAlreadyHaveAnAccount;

    @FXML
    private Button BtnSignUp;

    @FXML
    private Label ShowLblId;

    @FXML
    private AnchorPane SignInPane;

    @FXML
    private TextField TxtEmail;

    @FXML
    private TextField TxtFirstName;

    @FXML
    private TextField TxtLastName;

    @FXML
    private PasswordField TxtPwd;

    @FXML
    void BtnAlreadyHaveAnAccountOnAction(ActionEvent event) throws IOException {
        setUi("/view/LoginPage.fxml");
    }

    @FXML
    void PwdKeyTypeOnAction(KeyEvent event) {

    }

    @FXML
    void signUpOnClickAction(ActionEvent event) {
        try {
            String firstName = TxtFirstName.getText();
            String lastName = TxtLastName.getText();
            String email = TxtEmail.getText().toLowerCase();
            String password = TxtPwd.getText().trim(); // Directly use the password for now

            // Input validation
            if (email.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "All fields are required!").show();
                return;
            }

            // Create user
            UserDto createUser = new UserDto(firstName, lastName, email, password); // Use plain password for testing
            boolean isSaved = signup(createUser);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Welcome!").show();
                setUi("/view/LoginPage.fxml");
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong. Please try again!").show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to load the next screen!").show();
        }
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource(location))));
        stage.centerOnScreen();
    }

    private boolean signup(UserDto user) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        // write a SQl
        String sql ="INSERT INTO User VALUES (?,?,?,?)";
        System.out.println(connection);
        // INSERT INTO user VALUES('hiruna@gmail.com','Hiruna','Sankalpa','1234');
        // create statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        // set sql into the statement and execute
        return statement.executeUpdate()>0; // INSERT, UPDATE, DELETE


    }


}
