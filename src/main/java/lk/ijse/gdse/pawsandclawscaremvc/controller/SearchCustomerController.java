package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.pawsandclawscaremvc.dto.CustomerDto;
import lk.ijse.gdse.pawsandclawscaremvc.model.CustomerModel;
public class SearchCustomerController {

    @FXML
    private Button BtnBack;

    @FXML
    private AnchorPane content;

    @FXML
    private Button BtnSearch;

    @FXML
    private Label LblSearchAddress;

    @FXML
    private Label LblSearchCustId;

    @FXML
    private Label LblSearchCustName;

    @FXML
    private Label LblSearchEmail;

    @FXML
    private TextField TxtSearchCustContactNumber;

    CustomerModel customerModel = new CustomerModel();
    @FXML
    void BtnSearchOnClickAction(ActionEvent event) {
        String contactNumber = TxtSearchCustContactNumber.getText();

        if (!contactNumber.isEmpty()) {
            CustomerDto customer = customerModel.SearchCustomerByContact(contactNumber);
            LblSearchCustId.setText(contactNumber);
            if (customer != null) {
                LblSearchCustId.setText(customer.getCustomerId());
                LblSearchCustName.setText(customer.getCustomerName());
                LblSearchAddress.setText(customer.getAddress());
                LblSearchEmail.setText(customer.getEmail());
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Enter Valid Contact Number");
                alert.showAndWait();
            }
        }
    }

}
