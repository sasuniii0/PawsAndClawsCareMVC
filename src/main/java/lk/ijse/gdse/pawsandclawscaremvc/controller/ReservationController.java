package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.example.pawsandclawscare.db.DBConnection;
import org.example.pawsandclawscare.dto.ReservationDto;
import org.example.pawsandclawscare.dto.ServiceDetailsDto;
import org.example.pawsandclawscare.dto.tm.ReservationTm;
import org.example.pawsandclawscare.model.CustomerModel;
import org.example.pawsandclawscare.model.ReservationModel;
import org.example.pawsandclawscare.model.ServiceModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    @FXML
    private Button BtnAddNewCustomer;

    @FXML
    private Button BtnAddNewEmpl;

    @FXML
    private Button BtnCheckAvailability;

    @FXML
    private Button BtnConfirmRes;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnUpdate;

    @FXML
    private Label LblPrice;

    @FXML
    private ComboBox<String> CmbAvailableEmp;

    @FXML
    private ComboBox<String> CmbAvailableServices;

    @FXML
    private DatePicker CmbBoxDateSelecter;

    @FXML
    private ComboBox<String> CmbCustId;

    @FXML
    private TableColumn<?, ?> ColAction;

    @FXML
    private TableColumn<ReservationTm, String> ColCustId;

    @FXML
    private TableColumn<ReservationTm, String> ColDate;

    @FXML
    private TableColumn<ReservationTm, String> ColDesc;

    @FXML
    private TableColumn<ReservationTm, String> ColDropOffTime;

    @FXML
    private TableColumn<ReservationTm, String> ColPrice;

    @FXML
    private TableColumn<ReservationTm, String> ColResId;

    @FXML
    private TableColumn<ReservationTm, String> ColServiceId;

    @FXML
    private TableColumn<ReservationTm, Double> ColTotal;

    @FXML
    private Label LblCustName;

    @FXML
    private Label LblDate;

    @FXML
    private Label LblResId;

    @FXML
    private TableView<ReservationTm> TblReservation;

    @FXML
    private TextField TxtDesc;

    @FXML
    private TextField TxtDropOffTime;

    @FXML
    private AnchorPane contentReservation;

    private final ObservableList<ReservationTm> data = FXCollections.observableArrayList();

    @FXML
    void AddNewCustomerOnClickAction(ActionEvent event) {
        try{
            contentReservation.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/CustomerManage.fxml"));
            contentReservation.getChildren().add(anchorPane);
        }catch(IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load the page");
        }
    }

    @FXML
    void BtnAddNewEmplOnAction(ActionEvent event) {
        try{
            contentReservation.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/EmpManage.fxml"));
            contentReservation.getChildren().add(anchorPane);
        }catch(IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load the page");
        }
    }

    @FXML
    void BtnCheckAvailabilityOnAction(ActionEvent event) {
        try {
            LocalDate date = CmbBoxDateSelecter.getValue();
            String services = CmbAvailableServices.getValue();
            String dropOffTime = TxtDropOffTime.getText();

            if (date == null || services == null || services.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please Select Date or Service", ButtonType.OK).show();
                return;
            }

            System.out.println("Selected Date: " + date);
            System.out.println("Selected Service: " + services);
            System.out.println("Drop Off Time: " + dropOffTime);

            boolean isNotAvailable = reservationModel.checkServiceAvailability(date.toString(), services, dropOffTime);
            if (isNotAvailable) {
                new Alert(Alert.AlertType.ERROR, "Selected service is unavailable", ButtonType.OK).show();
                return;
            }

            ArrayList<String> availableEmployees = reservationModel.getAvailableEmployee();
            System.out.println("Available Employees: " + availableEmployees);  // Debugging

            if (!availableEmployees.isEmpty()) {
                ObservableList<String> employeeList = FXCollections.observableArrayList(availableEmployees);
                CmbAvailableEmp.setItems(employeeList);
                new Alert(Alert.AlertType.INFORMATION, "Availability confirmed", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.ERROR, "No Available Employees", ButtonType.OK).show();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to check Availability", ButtonType.OK).show();
        }
    }


    @FXML
    void CmbAvailabeServicesOnAction(ActionEvent event) throws SQLException {
        CmbAvailableServices.getSelectionModel().getSelectedItem();
        String selectedService = CmbAvailableServices.getValue();

        if (selectedService != null && !selectedService.contains("-")) {
            String [] parts = selectedService.split("-",2);
            String serviceId = parts[0].trim();
            String serviceName = parts[1].trim();
            new Alert(Alert.AlertType.INFORMATION, "Service Selected: " + selectedService, ButtonType.OK).show();
        }
        String price = reservationModel.getSelectedServicePrice(selectedService);
        LblPrice.setText(price);
    }

    @FXML
    void CmbAvailableEmpOnClickAction(ActionEvent event) {
        String selectedEmployee = CmbAvailableEmp.getValue();
        if (selectedEmployee != null) {
            new Alert(Alert.AlertType.INFORMATION, "Employee Selected: " + selectedEmployee, ButtonType.OK).show();
        }
    }

    @FXML
    void CmbBoxReservationDateOnClickAction(ActionEvent event) {
        LocalDate selectedDate = CmbBoxDateSelecter.getValue();
        if (selectedDate != null) {
            new Alert(Alert.AlertType.INFORMATION, "Date Selected: " + selectedDate, ButtonType.OK).show();
        }
    }

    @FXML
    void CmbCustIdOnClickAction(ActionEvent event) {
        try {
            String selectedCustId = CmbCustId.getValue();
            if (selectedCustId != null) {
                String customerName = customerModel.getCustomerNameById(selectedCustId);
                LblCustName.setText(customerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to fetch customer details.", ButtonType.OK).show();
        }
    }

    @FXML
    void ConfirmResBtnClickOnAction(ActionEvent event) {
        try {
            if (TblReservation.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Add All Fields", ButtonType.OK).show();
                return;
            }
            String resId = LblResId.getText();
            String selectedDate = String.valueOf(CmbBoxDateSelecter.getValue());
            String custId = CmbCustId.getValue();
            String dropOffTime = TxtDropOffTime.getText();
            String employee = CmbAvailableEmp.getValue();

            ArrayList<ServiceDetailsDto> serviceDetailsDtos = new ArrayList<>();

            for (ReservationTm reservationTm : reservationTms) {
                ServiceDetailsDto serviceDetailsDto = new ServiceDetailsDto(
                        reservationTm.getServiceId(),
                        resId,
                        reservationTm.getDescription()
                );
                serviceDetailsDtos.add(serviceDetailsDto);
            }
            ReservationDto reservationDto = new ReservationDto(
                    resId,
                    dropOffTime,
                    custId,
                    selectedDate,
                    employee,
                    serviceDetailsDtos
            );


            boolean isSaved = reservationModel.saveReservation(reservationDto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Reservation Saved", ButtonType.OK).show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Reservation Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void GenerateReportOnClickAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/Reports/ReservationReport.jrxml"
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
           e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        }
    }

    @FXML
    void ResetBtnOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void SaveBtnOnClickAction(ActionEvent event) throws SQLException {
        String resId = LblResId.getText();
        LocalDate selectedDate = CmbBoxDateSelecter.getValue();

        String selectedService = CmbAvailableServices.getValue();
        if (selectedService == null) {
            new Alert(Alert.AlertType.ERROR, "Please Select Service ", ButtonType.OK).show();
            return;
        }
        selectedService = selectedService.split("-")[0]; // Extract serviceId from "serviceId-description"

        String dropOffTime = TxtDropOffTime.getText();
        if (dropOffTime == null) {
            new Alert(Alert.AlertType.ERROR, "Please Select Drop Off Time", ButtonType.OK).show();
            return;
        }

        String custId = CmbCustId.getValue();
        if (custId == null) {
            new Alert(Alert.AlertType.ERROR, "Please Select Customer Id", ButtonType.OK).show();
            return;
        }

        String employee = CmbAvailableEmp.getValue();
        if (employee == null) {
            new Alert(Alert.AlertType.ERROR, "Please Select Employee ", ButtonType.OK).show();
            return;
        }
        employee = employee.split("-")[0];

        String desc = TxtDesc.getText();
        String price = LblPrice.getText();


        double pricePerHour = serviceModel.getPricePerHour(selectedService);
        String durationString = serviceModel.getDuration(selectedService); // Assume the format is "HH:mm:ss"
        String[] durationParts = durationString.split(":");
        int hours = Integer.parseInt(durationParts[0]);
        int minutes = Integer.parseInt(durationParts[1]);

        // Convert duration to fractional hours
        double durationInHours = hours + (minutes / 60.0);

        // Calculate total price
        double totalPrice = pricePerHour * durationInHours;

        for (ReservationTm reservationTm : reservationTms) {
            if (reservationTm.getServiceId().equals(selectedService)) {
                reservationTm.setTotal(totalPrice);
                TblReservation.refresh(); // Refresh the table view
                break;
            }
            new Alert(Alert.AlertType.INFORMATION, "Total Price Calculated: " + totalPrice, ButtonType.OK).show();
        }


        Button btn = new Button("Remove");
        ReservationTm reservationTm = new ReservationTm(
                resId,
                custId,
                selectedService,
                selectedDate,
                desc,
                dropOffTime,
                price,
                totalPrice,
                btn
        );
        btn.setOnAction(actionEvent -> {
            reservationTms.remove(reservationTm);
            TblReservation.refresh();
        });
        reservationTms.add(reservationTm);
    }

    @FXML
    void TblReservationOnClick(MouseEvent event) {

    }


    private final ReservationModel reservationModel = new ReservationModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final ServiceModel serviceModel = new ServiceModel();

    private final ObservableList<ReservationTm> reservationTms = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColResId.setCellValueFactory(new PropertyValueFactory<>("resId"));
        ColCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        ColServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        ColDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        ColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        ColDropOffTime.setCellValueFactory(new PropertyValueFactory<>("dropOffTime"));
        ColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        ColTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        ColAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        try{
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load the Reservation Id",ButtonType.OK).show();
        }
        TblReservation.setItems(reservationTms);

    }

    private void refreshPage() throws SQLException {
        LblResId.setText(reservationModel.getNextReservationId());
        LblDate.setText(LocalDate.now().toString());
        loadCustomerIds();
        loadAvailableServices();

        CmbBoxDateSelecter.setValue(LocalDate.now());
        CmbAvailableServices.getSelectionModel().clearSelection();
        CmbAvailableEmp.getSelectionModel().clearSelection();
        CmbCustId.getSelectionModel().clearSelection();
        TxtDesc.setText("");
        TxtDropOffTime.setText("");
        LblPrice.setText("");

        reservationTms.clear();
        TblReservation.refresh();
    }

    private void loadAvailableServices() {
        try {
            // Fetch the list of serviceId-description pairs from the model
            ArrayList<String> services = serviceModel.getAllServiceIdDesc();

            // Create an observable list from the services list
            ObservableList<String> serviceList = FXCollections.observableArrayList(services);

            // Set the observable list to the ComboBox
            CmbAvailableServices.setItems(serviceList);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load available services.", ButtonType.OK).show();
        }
    }


    private void loadCustomerIds() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        CmbCustId.setItems(observableList);
    }
}