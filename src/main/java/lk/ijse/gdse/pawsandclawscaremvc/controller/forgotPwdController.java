package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.pawsandclawscaremvc.util.security.VerificationCodeGenerator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class forgotPwdController {

    @FXML
    private Button BtnReturnLogIn;

    @FXML
    private Button BtnVerify;

    @FXML
    private Label ShowLblId;

    @FXML
    private AnchorPane SignInPane;

    @FXML
    private TextField TxtEmail;

    @FXML
    void BtnReturnLogInOnAction(ActionEvent event) throws IOException {
        Stage stage= (Stage) BtnVerify.getScene().getWindow();
        stage.close();
        Parent load = FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
        Scene scene = new Scene(load);
        Stage parentStage = new Stage();
        parentStage.setScene(scene);
        parentStage.show();
    }

    @FXML
    void BtnVerifyOnAction(ActionEvent event) {
        try {
            // Generate the verification code
            String verificationCode = new VerificationCodeGenerator().getCode(5);

            // Print the verification code to the terminal
            System.out.println("Verification Code: " + verificationCode);

            String fromEmail = "sasuni@gmail.com";
            String toEmail = TxtEmail.getText();
            String host = "127.0.0.1";

            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.port", "587");
            // => node=> nodemailer, (sendGrid, twilio)
            Session session = Session.getDefaultInstance(properties);

            //-------------------------
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setSubject("Verification Code");
            mimeMessage.setText("Verification Code : " + verificationCode);
            verifyAccController verify=new verifyAccController ();
            //verify.setUserData(Integer.parseInt(verificationCode),toEmail);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            verifyAccController .setUserData(String.valueOf(verificationCode),toEmail);

            // Uncomment this to send the email
            // Transport.send(mimeMessage);
            System.out.println("Email preparation completed!");

            //=======================>

            Stage window = (Stage) BtnVerify.getScene().getWindow();
            window.close();
            Parent load = FXMLLoader.load(getClass().getResource("/view/VerifyAccount.fxml"));
            Scene scene = new Scene(load);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
