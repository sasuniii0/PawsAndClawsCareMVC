package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.example.pawsandclawscare.dto.ServiceDto;
import org.example.pawsandclawscare.dto.tm.ServiceTm;
import org.example.pawsandclawscare.model.ServiceModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateRepService;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<ServiceTm, String> ColAvailability;

    @FXML
    private TableColumn<ServiceTm, String> ColDesc;

    @FXML
    private TableColumn<ServiceTm, String> ColDuration;

    @FXML
    private TableColumn<ServiceTm, Double> ColPrice;

    @FXML
    private TableColumn<ServiceTm, String> ColServiceId;

    @FXML
    private Label LblServiceId;

    @FXML
    private TableView<ServiceTm> TblService;

    @FXML
    private TextField TxtAvailability;

    @FXML
    private TextField TxtDesc;

    @FXML
    private TextField TxtDuration;

    @FXML
    private TextField TxtPrice;

    ServiceDto serviceDto = new ServiceDto();
    ServiceTm serviceTm = new ServiceTm();

    @FXML
    void DeleteBtnOnClickAction(ActionEvent event) throws SQLException {
        String serviceIdText = LblServiceId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = serviceModel.deleteService(serviceIdText);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Service Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
            }
        }
    }


    @FXML
    void ResetBtnOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void SaveBtnOnClickAction(ActionEvent event) throws SQLException {
        String serviceId = LblServiceId.getText();
        String availability = TxtAvailability.getText();
        String desc = TxtDesc.getText();
        Time duration = Time.valueOf(TxtDuration.getText());
        double price = Double.parseDouble(TxtPrice.getText());

        TxtAvailability.setStyle(TxtAvailability.getStyle() + " -fx-border-color: blue;");
        TxtDesc.setStyle(TxtDesc.getStyle() + " -fx-border-color: blue;");
        TxtDuration.setStyle(TxtDuration.getStyle() + " -fx-border-color: blue;");
        TxtPrice.setStyle(TxtPrice.getStyle() + " -fx-border-color: blue;");


            ServiceDto serviceDto = new ServiceDto(serviceId,availability,duration,price,desc);
            boolean isSaved = serviceModel.saveService(serviceDto);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Saving Failed").show();
            }

    }

    @FXML
    void TblServiceOnClick(MouseEvent event) {
        ServiceTm serviceTm = TblService.getSelectionModel().getSelectedItem();
        if (serviceTm != null) {
            LblServiceId.setText(serviceTm.getServiceId());
            TxtAvailability.setText(serviceTm.getAvailability());
            TxtDesc.setText(serviceTm.getDescription());
            TxtDuration.setText(String.valueOf(serviceTm.getDuration()));
            TxtPrice.setText(String.valueOf(serviceTm.getPrice()));

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @FXML
    void UpdateBtnOnClickAction(ActionEvent event) throws SQLException {
        String serviceId = LblServiceId.getText();
        String availability = TxtAvailability.getText();
        String desc = TxtDesc.getText();
        Time duration = Time.valueOf(TxtDuration.getText());
        double price = Double.parseDouble(TxtPrice.getText());

        TxtAvailability.setStyle(TxtAvailability.getStyle() + " -fx-border-color: blue;");
        TxtDesc.setStyle(TxtDesc.getStyle() + " -fx-border-color: blue;");
        TxtDuration.setStyle(TxtDuration.getStyle() + " -fx-border-color: blue;");
        TxtPrice.setStyle(TxtPrice.getStyle() + " -fx-border-color: blue;");


        ServiceDto serviceDto = new ServiceDto(serviceId,desc,duration,price,availability);
        boolean isUpdated = serviceModel.updateService(serviceDto);
        //System.out.println("hjbsqwhjs");
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Updated Successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Update Failed").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        ColAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        ColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        ColDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        ColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Service id", ButtonType.OK).show();
        }
        TblService.setRowFactory(new Callback<TableView<ServiceTm>, TableRow<ServiceTm>>() {
            @Override
            public TableRow<ServiceTm> call(TableView<ServiceTm> tableView) {
                final TableRow<ServiceTm> row = new TableRow<>();

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

    private void refreshPage() throws SQLException {
        loadNextServiceId();
        loadTableData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtAvailability.setText("");
        TxtDesc.setText("");
        TxtDuration.setText("");
        TxtPrice.setText("");
    }
    ServiceModel serviceModel = new ServiceModel();

    private void loadTableData() throws SQLException {
        ArrayList<ServiceDto> serviceDtos = serviceModel.getAllServices();
        ObservableList<ServiceTm> serviceTms = FXCollections.observableArrayList();

        for (ServiceDto serviceDto : serviceDtos) {
            ServiceTm serviceTm = new ServiceTm(
                    serviceDto.getServiceId(),
                    serviceDto.getAvailability(),
                    serviceDto.getDescription(),
                    serviceDto.getDuration(),
                    serviceDto.getPrice()
            );
            serviceTms.add(serviceTm);
        }
        TblService.setItems(serviceTms);
    }

    private void loadNextServiceId() throws SQLException {
        String nextServiceId = serviceModel.getNextCustomerId();
        LblServiceId.setText(nextServiceId);
    }
}
