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
import org.example.pawsandclawscare.dto.PaymentDto;
import org.example.pawsandclawscare.dto.tm.PaymentTm;
import org.example.pawsandclawscare.model.PaymentModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentController implements Initializable {

    @FXML
    private Label LblCustId;

    @FXML
    private Label LblEmail;

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnPrintRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSearch;

    @FXML
    private Button BtnSendMail;

    @FXML
    private Button BtnUpdate;

    @FXML
    private ComboBox<String> CmbOrderId;

    @FXML
    private ComboBox<String> CmbReservationId;

    @FXML
    private TableColumn<PaymentTm, Double> ColAmount;

    @FXML
    private TableColumn<PaymentTm, String> ColCustId;

    @FXML
    private TableColumn<PaymentTm, String> ColEmail;

    @FXML
    private TableColumn<PaymentTm, String> ColOrderId;

    @FXML
    private TableColumn<PaymentTm, Date> ColPayementDate;

    @FXML
    private TableColumn<PaymentTm, String> ColPayementMethod;

    @FXML
    private TableColumn<PaymentTm, String> ColPaymentId;

    @FXML
    private TableColumn<PaymentTm, String> ColResId;

    @FXML
    private Label LblPaymentDate;

    @FXML
    private Label LblPaymentId;

    @FXML
    private RadioButton RadioCard;

    @FXML
    private RadioButton RadioCash;

    @FXML
    private RadioButton RadioOrder;

    @FXML
    private RadioButton RadioReservation;

    @FXML
    private TableView<PaymentTm> TblPayment;

    @FXML
    private ToggleGroup TglPaymentMethod;

    @FXML
    private ToggleGroup TglPaymentType;

    @FXML
    private TextField TxtAmount;

    @FXML
    private TextField TxtSearchBox;

    SendPaymentMailController sendPaymentMailController = new SendPaymentMailController();

    @FXML
    void BtnAddOnAction(ActionEvent event) throws SQLException {
        String paymentId = LblPaymentId.getText();
        LocalDate date = LocalDate.parse(LblPaymentDate.getText());
        String paymentType = RadioReservation.isSelected() ? "reservation" : "order";
        String resId = RadioReservation.isSelected() ? CmbReservationId.getValue() : null;
        String orderId = RadioOrder.isSelected() ? CmbOrderId.getValue() : null;

        String method = RadioCash.isSelected() ? "Cash" : "Card";

        double amount = Double.parseDouble(TxtAmount.getText());
        TxtAmount.setStyle(TxtAmount.getStyle()+"-fx-border-color: blue;");
        String custId =LblCustId.getText();
        String email= LblEmail.getText();

        PaymentDto paymentDto = new PaymentDto(paymentId,date,amount,method,resId,orderId,custId,email);
        boolean isSaved = paymentModel.savePayment(paymentDto);
        if(isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Payment added successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to save Payment").show();
        }
    }

    @FXML
    void BtnDeleteOnAction(ActionEvent event) throws SQLException {
        String paymentIdTxt = LblPaymentId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = paymentModel.deletePayment(paymentIdTxt);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Payment Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
            }
        }
    }

    @FXML
    void BtnPrintRepOnAction(ActionEvent event) {
        PaymentTm paymentTm = TblPayment.getSelectionModel().getSelectedItem();

        if (paymentTm == null) {
            return;
        }

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/Reports/PaymentReport.jrxml"));

            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            //System.out.println(parameters);

            parameters.put("P_Date", String.valueOf(LocalDate.now()));
            parameters.put("P_custId", paymentTm.getCustId());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to generate report...!").show();
          e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        }
    }

    @FXML
    void BtnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void BtnSearchOnAction(ActionEvent event) {
        String searchText = TxtSearchBox.getText().toLowerCase();  // Get the text from the search field

        try {
            // Call the method to search for products by catalog
            ArrayList<PaymentDto> filteredPayments = paymentModel.searchPaymentsByEmail(searchText);

            // Convert the filtered products to ProductTm objects
            ObservableList<PaymentTm> filteredList = FXCollections.observableArrayList();
            for (PaymentDto paymentDto : filteredPayments) {
                PaymentTm paymentTm = new PaymentTm(
                        paymentDto.getPaymentId(),
                        paymentDto.getDate(),
                        paymentDto.getAmount(),
                        paymentDto.getMethod(),
                        paymentDto.getResId(),
                        paymentDto.getOrderId(),
                        paymentDto.getCustId(),
                        paymentDto.getEmail()
                );
                filteredList.add(paymentTm);
            }

            // Update the TableView with the filtered list
            TblPayment.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while searching Payment", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void BtnSendMailOnAction(ActionEvent event) {
        PaymentTm selectedItem = TblPayment.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select email..!");
            return;
        }

        try {
            // Load the mail dialog from FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SendPaymentMail.fxml"));
            Parent load = loader.load();

            SendPaymentMailController sendPaymentMailController = loader.getController();

            String email = selectedItem.getEmail();
            sendPaymentMailController.setCustMail(email);

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
    void BtnUpdateOnAction(ActionEvent event) throws SQLException {
        String paymentId = LblPaymentId.getText();
        LocalDate date = LocalDate.parse(LblPaymentDate.getText());
        String paymentType = RadioReservation.isSelected() ? "reservation" : "order";
        String resId = RadioReservation.isSelected() ? CmbReservationId.getValue() : null;
        String orderId = RadioOrder.isSelected() ? CmbOrderId.getValue() : null;

        String method = RadioCash.isSelected() ? "Cash" : "Card";

        double amount = Double.parseDouble(TxtAmount.getText());
        TxtAmount.setStyle(TxtAmount.getStyle()+"-fx-border-color: blue;");
        String custId =LblCustId.getText();
        String email= LblEmail.getText();

        PaymentDto paymentDto = new PaymentDto(paymentId,date,amount,method,resId,orderId,custId,email);
        boolean isSaved = paymentModel.UpdatePayment(paymentDto);
        if(isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Payment Updated successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to update Payment").show();
        }
    }

    @FXML
    void CmbOrderIdOnAction(ActionEvent event) {
        CmbOrderId.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    paymentModel.displayCustomerDetailsByOrderId(newValue);// Fetch and display details based on orderId
                    LblCustId.setText(newValue);
                    LblEmail.setText(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void CmbReservationIdOnAction(ActionEvent event) {
        CmbReservationId.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    paymentModel.displayCustomerDetailsByResId(newValue); // Fetch and display details based on resId
                    LblCustId.setText(newValue);
                    LblEmail.setText(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void RadioCardOnAction(ActionEvent event) {

    }

    @FXML
    void RadioCashOnAction(ActionEvent event) {

    }

    @FXML
    void RadioOrderOnAction(ActionEvent event) {

    }

    @FXML
    void RadioReservationOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        ColPayementDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        ColPayementMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
        ColAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        ColOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ColResId.setCellValueFactory(new PropertyValueFactory<>("resId"));
        ColCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        ColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load PaymentId",ButtonType.OK).show();
        }
        ToggleGroup tglPaymentMethod = new ToggleGroup();
        RadioCard.setToggleGroup(tglPaymentMethod);
        RadioCash.setToggleGroup(tglPaymentMethod);

        ToggleGroup tglPaymentType = new ToggleGroup();
        RadioOrder.setToggleGroup(tglPaymentType);
        RadioReservation.setToggleGroup(tglPaymentType);

        RadioCash.setOnAction(event -> handleMethodRadioSelection());
        RadioCard.setOnAction(event -> handleMethodRadioSelection());
        RadioOrder.setOnAction(event -> handleTypeRadioSelection());
        RadioReservation.setOnAction(event -> handleTypeRadioSelection());

        handleMethodRadioSelection();
        handleTypeRadioSelection();
        TblPayment.setRowFactory(new Callback<TableView<PaymentTm>, TableRow<PaymentTm>>() {
            @Override
            public TableRow<PaymentTm> call(TableView<PaymentTm> tableView) {
                final TableRow<PaymentTm> row = new TableRow<>();

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

    private void handleTypeRadioSelection() {
        if (RadioOrder.isSelected()) {
            // Enable Service fields and disable Order fields
            loadOrderData();
            CmbOrderId.setDisable(false);

            CmbReservationId.setDisable(true);
        } else if (RadioReservation.isSelected()) {
            // Enable Order fields and disable Service fields
            loadReservationData();
            CmbReservationId.setDisable(false);
            CmbOrderId.setDisable(true);
        }
    }

    private void loadReservationData() {
        try {
            ObservableList<String> serviceIds = paymentModel.getAllReservationIds();
            CmbReservationId.setItems(serviceIds);
            CmbReservationId.valueProperty().addListener((obs, oldValue, newValue) -> {
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load reservation data: " + e.getMessage()).show();
        }
    }
PaymentModel paymentModel = new PaymentModel();
    private void loadOrderData() {
        try {
            ObservableList<String> orderIds = paymentModel.getAllOrderIds();
            CmbOrderId.setItems(orderIds);
            CmbOrderId.valueProperty().addListener((obs, oldValue, newValue) -> {
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load order data: " + e.getMessage()).show();
        }
    }

    private void handleMethodRadioSelection() {
        if (RadioCard.isSelected()) {

        } else if (RadioCash.isSelected()) {

        }
    }


    private void refreshPage() throws SQLException {
        loadNextPaymentId();
        loadTableData();
        LocalDate date =LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Customize the pattern as needed
        String formattedDate = date.format(formatter);

        // Set the formatted date as the text of the label
        LblPaymentDate.setText(formattedDate);

        BtnAdd.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        CmbReservationId.getSelectionModel().clearSelection();
        CmbOrderId.getSelectionModel().clearSelection();

        TxtAmount.setText("");
        TxtSearchBox.setText("");
        RadioCash.setSelected(false);
        RadioCard.setSelected(false);
        RadioOrder.setSelected(false);
        RadioReservation.setSelected(false);
    }

    private void loadTableData() throws SQLException {
        ArrayList<PaymentDto> paymentDto = paymentModel.getAllPayments();

        ObservableList<PaymentTm> paymentTms = FXCollections.observableArrayList();

        for (PaymentDto paymentDtos : paymentDto) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(paymentDtos.getDate());
            PaymentTm paymentTm = new PaymentTm(
                    paymentDtos.getPaymentId(),
                    sqlDate.toLocalDate(),
                    paymentDtos.getAmount(),
                    paymentDtos.getMethod(),
                    paymentDtos.getResId(),
                    paymentDtos.getOrderId(),
                    paymentDtos.getCustId(),
                    paymentDtos.getEmail()
            );
            paymentTms.add(paymentTm);
        }
        TblPayment.setItems(paymentTms);
    }

    private void loadNextPaymentId() throws SQLException {
        String nextEmpId = paymentModel.getNextPaymentId();
        LblPaymentId.setText(nextEmpId);
    }

    public void TblPaymentOnClick(MouseEvent mouseEvent) {
        PaymentTm paymentTm =TblPayment.getSelectionModel().getSelectedItem();
        if (paymentTm != null) {
            LblPaymentId.setText(paymentTm.getPaymentId());
            CmbOrderId.setValue(paymentTm.getOrderId());
            CmbReservationId.setValue(paymentTm.getCustId());
            LblPaymentDate.setText(String.valueOf(paymentTm.getPaymentDate()));
            TxtAmount.setText(String.valueOf(paymentTm.getPaymentAmount()));
            TglPaymentMethod.selectedToggleProperty();
            TglPaymentType.selectedToggleProperty();

            BtnAdd.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }
}
