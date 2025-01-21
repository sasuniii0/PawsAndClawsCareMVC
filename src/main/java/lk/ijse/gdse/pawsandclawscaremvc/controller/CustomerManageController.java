package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.example.pawsandclawscare.db.DBConnection;
import org.example.pawsandclawscare.dto.CustomerDto;
import org.example.pawsandclawscare.dto.tm.CustomerTm;
import org.example.pawsandclawscare.model.CustomerModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerManageController implements Initializable {

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateAllCustomers;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnSendMail;

    @FXML
    private Button BtnUpdate;

    @FXML
    private Button BtnSearchCustomer;

    @FXML
    private TableColumn<CustomerTm, String> ColAddress;

    @FXML
    private TableColumn<CustomerTm, String> ColContactNumber;

    @FXML
    private TableColumn<CustomerTm, String> ColCustId;

    @FXML
    private TableColumn<CustomerTm, String> ColCustName;

    @FXML
    private TableColumn<CustomerTm, String> ColEmail;

    @FXML
    private Label LblAddress;

    @FXML
    private Label LblContactNumber;

    @FXML
    private Label LblCustId;

    @FXML
    private Label LblCustName;

    @FXML
    private Label LblEmail;

    @FXML
    private TableView<CustomerTm> TblCustomer;

    @FXML
    private TextField TxtAddress;

    @FXML
    private TextField TxtContactNumber;

    @FXML
    private TextField TxtCustomerName;

    @FXML
    private TextField TxtEmail;

    @FXML
    void DeleteOnClickAction(ActionEvent event) throws SQLException {
        String customerId = LblCustId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = customerModel.deleteCustomer(customerId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
            }
        }
    }

    @FXML
    void GenerateAllCustomerOnClickAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/Reports/Customer.jrxml"
                            ));

            Connection connection = DBConnection.getInstance().getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to generate report...!").show();
//           e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        }
    }


    @FXML
    void ResetOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    CustomerModel customerModel = new CustomerModel();
    @FXML
    void SaveOnClickAction(ActionEvent event) throws SQLException {
        String custId = LblCustId.getText();
        String custName = TxtCustomerName.getText();
        String address = TxtAddress.getText();
        String email = TxtEmail.getText();
        String contactNumber = TxtContactNumber.getText();

        TxtCustomerName.setStyle(TxtCustomerName.getStyle() + " -fx-border-color: blue;");
        TxtAddress.setStyle(TxtAddress.getStyle() + " -fx-border-color: blue;");
        TxtEmail.setStyle(TxtEmail.getStyle() + " -fx-border-color: blue;");
        TxtContactNumber.setStyle(TxtContactNumber.getStyle() + " -fx-border-color: blue;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[a-zA-Z0-9_.\\- ]+$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = custName.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidContact = contactNumber.matches(contactNumber);

        if (!isValidName){
            TxtCustomerName.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidAddress){
            TxtAddress.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidEmail){
            TxtEmail.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidContact){
            TxtContactNumber.setStyle("-fx-border-color: red;");
            return;
        }
        if (isValidEmail && isValidContact && isValidName && isValidAddress){
            CustomerDto customerDto = new CustomerDto(custId, custName, address, email, contactNumber);

            boolean isSaved = customerModel.saveCustomer(customerDto);
            if (isSaved){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Customer Saved....").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to save Customer...").show();
            }
        }

    }

    private void refreshPage() throws SQLException {
        loadNextCustomerId();
        loadTableData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtCustomerName.setText("");
        TxtAddress.setText("");
        TxtEmail.setText("");
        TxtContactNumber.setText("");
    }

    private void loadTableData() throws SQLException {
        ArrayList<CustomerDto> customerDtos = customerModel.getAllCustomers();

        ObservableList<CustomerTm> customerTms = FXCollections.observableArrayList();

        for (CustomerDto customerDto : customerDtos) {
            CustomerTm customerTm = new CustomerTm(
                    customerDto.getCustomerId(),
                    customerDto.getCustomerName(),
                    customerDto.getAddress(),
                    customerDto.getEmail(),
                    customerDto.getContactNumber()
            );
            customerTms.add(customerTm);
        }
        TblCustomer.setItems(customerTms);
    }

    private void loadNextCustomerId() throws SQLException {
        String nextCustomerId = customerModel.getNextCustomerId();
        LblCustId.setText(nextCustomerId);
    }

    CustMailSenderController custMailSenderController = new CustMailSenderController();

    @FXML
    void SendMailOnClickAction(ActionEvent event) {
        CustomerTm selectedItem = TblCustomer.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select customer..!");
            return;
        }

        try {
            // Load the mail dialog from FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustMailSend.fxml"));
            Parent load = loader.load();

            CustMailSenderController sendMailController = loader.getController();

            String email = selectedItem.getEmail();
            sendMailController.setCustEmail(email);

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Send email");
           // stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/mail_icon.png")));

            // Set window as modal
            stage.initModality(Modality.APPLICATION_MODAL);

            Window underWindow = BtnUpdate.getScene().getWindow();
            stage.initOwner(underWindow);

            stage.showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load ui..!");
            e.printStackTrace();
        }
    }

    @FXML
    void UpdateOnClickAction(ActionEvent event) throws SQLException {
        String custId = LblCustId.getText();
        String custName = TxtCustomerName.getText();
        String address = TxtAddress.getText();
        String email = TxtEmail.getText();
        String contactNumber = TxtContactNumber.getText();

        TxtCustomerName.setStyle(TxtCustomerName.getStyle() + " -fx-border-color: blue;");
        TxtAddress.setStyle(TxtAddress.getStyle() + " -fx-border-color: blue;");
        TxtEmail.setStyle(TxtEmail.getStyle() + " -fx-border-color: blue;");
        TxtContactNumber.setStyle(TxtContactNumber.getStyle() + " -fx-border-color: blue;");

        String namePattern = "^[A-Za-z ]+$";
        String addressPattern = "^[a-zA-Z0-9_.\\- ]+$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidName = custName.matches(namePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidContact = contactNumber.matches(contactNumber);

        if (!isValidName){
            TxtCustomerName.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidAddress){
            TxtAddress.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidEmail){
            TxtEmail.setStyle("-fx-border-color: red;");
            return;
        }
        if (!isValidContact){
            TxtContactNumber.setStyle("-fx-border-color: red;");
            return;
        }
        if (isValidEmail && isValidContact && isValidName && isValidAddress){
            CustomerDto customerDto = new CustomerDto(custId, custName, address, email, contactNumber);

            boolean isSaved = customerModel.updateCustomer(customerDto);
            if (isSaved){
                refreshPage();

                new Alert(Alert.AlertType.INFORMATION,"Customer Updated....").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to update Customer...").show();
            }
        }
    }

    @FXML
    void TableOnClickAction(MouseEvent event) {
        CustomerTm customerTm = (CustomerTm) TblCustomer.getSelectionModel().getSelectedItem();
        if (customerTm != null) {
            LblCustId.setText(customerTm.getCustomerId());
            TxtCustomerName.setText(customerTm.getCustomerName());
            TxtAddress.setText(customerTm.getAddress());
            TxtEmail.setText(customerTm.getEmail());
            TxtContactNumber.setText(customerTm.getContactNumber());

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ColCustName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        ColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        ColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        ColContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Load CustomerId").show();
        }
        TblCustomer.setRowFactory(new Callback<TableView<CustomerTm>, TableRow<CustomerTm>>() {
            @Override
            public TableRow<CustomerTm> call(TableView<CustomerTm> tableView) {
                final TableRow<CustomerTm> row = new TableRow<>();

                // Add listener for row selection change
                row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        // Set the background color of the selected row
                        row.setStyle("-fx-background-color: #00d2d3;"); // Coral Red for selected row
                    } else {
                        // Reset the background color when the row is deselected
                        row.setStyle("");
                    }
                });

                return row;
            }
        });
    }

    public void BtnSearchCustomerOnClickAction(ActionEvent actionEvent) throws IOException {
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchCustomer.fxml"));
           Parent load = loader.load();

           Stage stage = new Stage();
           stage.setScene(new Scene(load));
           stage.setTitle("Search Customer");
           stage.initModality(Modality.APPLICATION_MODAL);

           Window underWindow = BtnUpdate.getScene().getWindow();
           stage.initOwner(underWindow);

           stage.showAndWait();
       }catch (IOException e) {
           new Alert(Alert.AlertType.ERROR, "Fail to load ui..!");
           e.printStackTrace();
       }
    }
}
