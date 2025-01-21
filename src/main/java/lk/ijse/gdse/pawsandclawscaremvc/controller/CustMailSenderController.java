package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class CustMailSenderController {

    @FXML
    private Button BtnSend;

    @Setter
    @FXML
    private String CustEmail;

    @FXML
    private TextArea TxtBody;

    @FXML
    private TextField TxtSub;

    @FXML
    void BtnSendOnAction(ActionEvent event) {
        if (CustEmail == null || CustEmail.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Recipient email is not set!").show();
            return;
        }

        // The sender's email address
        final String FROM = "sasuniwijerathne@gmail.com";

        // Get the subject and body from the text fields
        String subject = TxtSub.getText();
        String body = TxtBody.getText();

        if (subject.isEmpty() || body.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Subject and body must not be empty!").show();
            return;
        }

        // Call the method to send an email via SendGrid
        sendEmailWithSendgrid(FROM, CustEmail, subject, body);
    }

    private void sendEmailWithSendgrid(String from, String to, String subject, String body) {
        final String USER_NAME = "apikey"; // SendGrid's requirement
        final String PASSWORD = "replace your apikey";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.sendgrid.net");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_NAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

            new Alert(Alert.AlertType.INFORMATION, "Email sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send email: " + e.getMessage()).show();
        }
    }
}
