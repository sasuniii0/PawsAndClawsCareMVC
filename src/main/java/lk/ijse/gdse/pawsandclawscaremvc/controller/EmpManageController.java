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
import javafx.util.Callback;
import lk.ijse.gdse.pawsandclawscaremvc.dto.EmployeeDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.tm.EmployeeTm;
import lk.ijse.gdse.pawsandclawscaremvc.model.EmpManageModel;
import lk.ijse.gdse.pawsandclawscaremvc.model.OrderManageModel;
import lk.ijse.gdse.pawsandclawscaremvc.model.ServiceModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmpManageController implements Initializable {

    @FXML
    private Button BtnAddNewOrder;

    @FXML
    private Button BtnAddNewService;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnSearch;

    @FXML
    private Button BtnSendMail;

    @FXML
    private Button BtnUpdate;

    @FXML
    private ComboBox<String> CmbOrdersId;

    @FXML
    private ComboBox<String> CmbServiceId;

    @FXML
    private TableColumn<EmployeeTm, String> ColContactNumber;

    @FXML
    private TableColumn<EmployeeTm, String> ColEmpId;

    @FXML
    private TableColumn<EmployeeTm, String> ColEmployeeType;

    @FXML
    private TableColumn<EmployeeTm, String> ColOrderId;

    @FXML
    private TableColumn<EmployeeTm, String> ColEmployeeRole;

    @FXML
    private TableColumn<EmployeeTm, String> ColServiceId;

    @FXML
    private TableColumn<EmployeeTm, String> ColStartTime;

    @FXML
    private TableColumn<EmployeeTm, String> ColEndTime;

    @FXML
    private Label LblDate;

    @FXML
    private Label LblDesc;

    @FXML
    private Label LblEmpId;

    @FXML
    private RadioButton RadioOrders;

    @FXML
    private RadioButton RadioService;

    @FXML
    private TableView<EmployeeTm> TblEmployee;

    @FXML
    private ToggleGroup TogEmployee;

    @FXML
    private TextField TxtContactNumber;

    @FXML
    private TextField TxtRoll;

    @FXML
    private TextField TxtStartTime;

    @FXML
    private TextField TxtEndTime;

    @FXML
    private TextField TxtSearchEmpRole;

    @FXML
    private AnchorPane display;

    EmpManageModel empManageModel = new EmpManageModel();

    @FXML
    void BtnAddNewOrderOnClickAction(ActionEvent event) {
        try {
            AnchorPane servicePane = FXMLLoader.load(getClass().getResource("/view/OrderManage.fxml"));
            display.getChildren().setAll(servicePane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load Order page: " + e.getMessage()).show();
        }
    }

    @FXML
    void BtnAddNewServiceOnClickAction(ActionEvent event) {
        try {
            AnchorPane servicePane = FXMLLoader.load(getClass().getResource("/view/Services.fxml"));
            display.getChildren().setAll(servicePane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load service page: " + e.getMessage()).show();
        }
    }

    @FXML
    void BtnDeleteOnClickAction(ActionEvent event) throws SQLException {
        String serviceIdText = LblEmpId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = empManageModel.deleteService(serviceIdText);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Service Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
            }
        }
    }

    @FXML
    void BtnResetOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void BtnSaveOnClickAction(ActionEvent event) throws SQLException {
        String empId = LblEmpId.getText();
        String employeeType = RadioService.isSelected() ? "service" : "order";
        String serviceId = RadioService.isSelected() ? CmbServiceId.getValue() : null;
        String orderId = RadioOrders.isSelected() ? CmbOrdersId.getValue() : null;
        String role = TxtRoll.getText();
        String contactNumber = TxtContactNumber.getText();
        String startTime = TxtStartTime.getText();
        String endTime = TxtEndTime.getText();


        TxtRoll.setStyle(TxtRoll.getStyle()+"-fx-border-color: blue;");
        TxtStartTime.setStyle(TxtStartTime.getStyle()+"-fx-border-color: blue;");
        TxtEndTime.setStyle(TxtEndTime.getStyle()+"-fx-border-color: blue;");
        TxtContactNumber.setStyle(TxtContactNumber.getStyle()+"-fx-border-color: blue;");

        EmployeeDto employeeDto = new EmployeeDto(empId,startTime,endTime,role,contactNumber,serviceId,orderId,employeeType);

        boolean isSaved = empManageModel.saveEmployee(employeeDto);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to save employee").show();
        }
    }

    @FXML
    void BtnSearchOnClickAction(ActionEvent event) {
        String searchText = TxtSearchEmpRole.getText().toLowerCase();  // Get the text from the search field

        try {
            // Call the method to search for products by catalog
            ArrayList<EmployeeDto> filteredEmployee = empManageModel.searchEmployeeByRole(searchText);

            // Convert the filtered products to ProductTm objects
            ObservableList<EmployeeTm> filteredList = FXCollections.observableArrayList();
            for (EmployeeDto employeeDto : filteredEmployee) {
                EmployeeTm employeeTm = new EmployeeTm(
                        employeeDto.getEmpId(),
                        employeeDto.getStartTime(),
                        employeeDto.getEndTime(),
                        employeeDto.getRole(),
                        employeeDto.getContactNumber(),
                        employeeDto.getServiceId(),
                        employeeDto.getOrderId(),
                        employeeDto.getEmployeeType()
                );
                filteredList.add(employeeTm);
            }

            // Update the TableView with the filtered list
            TblEmployee.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while searching Employee", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void BtnUpdateOnClickAction(ActionEvent event) throws SQLException {
        String empId = LblEmpId.getText();
        String employeeType = RadioService.isSelected() ? "service" : "order";
        String serviceId = RadioService.isSelected() ? CmbServiceId.getValue() : null;
        String orderId = RadioOrders.isSelected() ? CmbOrdersId.getValue() : null;
        String role = TxtRoll.getText();
        String contactNumber = TxtContactNumber.getText();
        String startTime = TxtStartTime.getText();
        String endTime = TxtEndTime.getText();


        TxtRoll.setStyle(TxtRoll.getStyle()+"-fx-border-color: blue;");
        TxtStartTime.setStyle(TxtEndTime.getStyle()+"-fx-border-color: blue;");
        TxtEndTime.setStyle(TxtEndTime.getStyle()+"-fx-border-color: blue;");
        TxtContactNumber.setStyle(TxtContactNumber.getStyle()+"-fx-border-color: blue;");

        EmployeeDto employeeDto = new EmployeeDto(empId,startTime,endTime,role,contactNumber,serviceId,orderId,employeeType);

        boolean isSaved = empManageModel.updateEmployee(employeeDto);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to update employee").show();
        }
    }
OrderManageModel orderManageModel = new OrderManageModel();
    @FXML
    void CmbOrderId(ActionEvent event) {
        String selectedOrderId = CmbOrdersId.getValue();

        if (selectedOrderId != null) {
            try {
                // Fetch order date from the database
                String orderDate = orderManageModel.getOrderDateById(selectedOrderId);

                // Update the label
                LblDate.setText(orderDate != null ? orderDate : "Order date not available");
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to fetch order date", ButtonType.OK).show();
            }
        }
    }
ServiceModel serviceModel = new ServiceModel();
    @FXML
    void CmbServiceId(ActionEvent event) {
        String selectedServiceId = CmbServiceId.getValue();

        if (selectedServiceId != null) {
            try {
                // Fetch service description from the database
                String serviceDescription = serviceModel.getServiceDescriptionById(selectedServiceId);
                // Update the label
                LblDesc.setText(serviceDescription != null ? serviceDescription : "Description not available");
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to fetch service description", ButtonType.OK).show();
            }
        }
    }

    @FXML
    void TblEmployeeOnClickAction(MouseEvent event) {
        EmployeeTm employeeTm =TblEmployee.getSelectionModel().getSelectedItem();
        if (employeeTm != null) {
            LblEmpId.setText(employeeTm.getEmpId());
            TxtStartTime.setText(employeeTm.getStartTime());
            TxtEndTime.setText(employeeTm.getEndTime());
            TxtRoll.setText(employeeTm.getRole());
            TxtContactNumber.setText(employeeTm.getContactNumber());
            CmbServiceId.setValue(employeeTm.getServiceId());
            CmbOrdersId.setValue(employeeTm.getOrderId());


            TogEmployee.getToggles().stream()
                    .filter(toggle -> toggle.getUserData().toString().equals(employeeTm.getEmployeeType()))
                    .findFirst()
                    .ifPresent(toggle -> TogEmployee.selectToggle(toggle));

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        ColStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        ColEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        ColEmployeeRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        ColContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        ColServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        ColOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ColEmployeeType.setCellValueFactory(new PropertyValueFactory<>("employeeType"));

        try{
            refreshPage();
            loadEmployeeData();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Load Employee Id").show();
        }
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioService.setToggleGroup(toggleGroup);
        RadioOrders.setToggleGroup(toggleGroup);

        // Add listeners to RadioButtons
        RadioService.setOnAction(event -> handleRadioSelection());
        RadioOrders.setOnAction(event -> handleRadioSelection());

        // Initial state
        handleRadioSelection();
        TblEmployee.setRowFactory(new Callback<TableView<EmployeeTm>, TableRow<EmployeeTm>>() {
            @Override
            public TableRow<EmployeeTm> call(TableView<EmployeeTm> tableView) {
                final TableRow<EmployeeTm> row = new TableRow<>();

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

    private void loadEmployeeData() throws SQLException {
        // Clear existing data
        TblEmployee.getItems().clear();

        // Fetch all employees from the model
        ArrayList<EmployeeDto> employees = empManageModel.getAllEmployees();

        // Convert EmployeeDto objects to EmployeeTm objects
        ObservableList<EmployeeTm> employeeList = FXCollections.observableArrayList();
        System.out.println(employeeList);
        for (EmployeeDto employee : employees) {
            employeeList.add(new EmployeeTm(
                    employee.getEmpId(),
                    employee.getRole(),
                    employee.getContactNumber(),
                    employee.getServiceId(),
                    employee.getOrderId(),
                    employee.getEmployeeType(),
                    employee.getStartTime(),
                    employee.getEndTime()
            ));
        }
        // Set data to the TableView
        //System.out.println(employeeList);
        TblEmployee.setItems(employeeList);
    }

    private void handleRadioSelection() {
        if (RadioService.isSelected()) {
            // Enable Service fields and disable Order fields
            loadServiceData();
            CmbServiceId.setDisable(false);
            LblDesc.setDisable(false);

            CmbOrdersId.setDisable(true);
            LblDate.setDisable(true);
        } else if (RadioOrders.isSelected()) {
            // Enable Order fields and disable Service fields
            loadOrderData();
            CmbServiceId.setDisable(true);
            LblDesc.setDisable(true);

            CmbOrdersId.setDisable(false);
            LblDate.setDisable(false);
        }
    }

    private void loadOrderData() {
        try {
            ObservableList<String> orderIds = empManageModel.getAllOrderIds();
            CmbOrdersId.setItems(orderIds);
            CmbOrdersId.valueProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    LblDate.setText(empManageModel.getOrderDate(newValue).toString());
                } catch (SQLException e) {
                   // LblDate.setText("");
                }
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load order data: " + e.getMessage()).show();
        }
    }

    private void loadServiceData() {
        try {
            ObservableList<String> serviceIds = empManageModel.getAllServiceIds();
            CmbServiceId.setItems(serviceIds);
            CmbServiceId.valueProperty().addListener((obs, oldValue, newValue) -> {
                try {
                    LblDesc.setText(empManageModel.getServiceDescription(newValue));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load service data: " + e.getMessage()).show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextEmplId();
        loadTblData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);
        
        CmbServiceId.getSelectionModel().clearSelection();
        CmbOrdersId.getSelectionModel().clearSelection();
        TxtRoll.setText("");
        TxtStartTime.setText("");
        TxtEndTime.setText("");
        TxtSearchEmpRole.setText("");
        TxtContactNumber.setText("");
        LblDesc.setText("");
        LblDate.setText("");
        RadioOrders.setSelected(false);
        RadioService.setSelected(false);

    }

    private void loadTblData() throws SQLException {
        ArrayList<EmployeeDto> employeeDto = empManageModel.getAllEmployees();

        ObservableList<EmployeeTm> employeeTms = FXCollections.observableArrayList();

        for (EmployeeDto employeeDtos : employeeDto) {
            EmployeeTm employeeTm = new EmployeeTm(
                    employeeDtos.getEmpId(),
                    employeeDtos.getRole(),
                    employeeDtos.getContactNumber(),
                    employeeDtos.getServiceId(),
                    employeeDtos.getOrderId(),
                    employeeDtos.getEmployeeType(),
                    employeeDtos.getStartTime(),
                    employeeDtos.getEndTime()
            );
            employeeTms.add(employeeTm);
        }

        TblEmployee.setItems(employeeTms);
    }

    private void loadNextEmplId() throws SQLException {
        String nextEmpId = empManageModel.getNextEmpId();
        LblEmpId.setText(nextEmpId);
    }
}
