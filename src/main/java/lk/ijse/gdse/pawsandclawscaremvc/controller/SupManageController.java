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
import org.example.pawsandclawscare.dto.SupplierDto;
import org.example.pawsandclawscare.dto.tm.SupplierTm;
import org.example.pawsandclawscare.model.InvenManageModel;
import org.example.pawsandclawscare.model.SupManageModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupManageController implements Initializable {

    @FXML
    private Button BtnAddSup;

    @FXML
    private Button BtnSearch;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<SupplierTm, String> ColContactNumber;

    @FXML
    private TableColumn<SupplierTm, String> ColSupId;

    @FXML
    private TableColumn<SupplierTm, String> ColSupName;

    @FXML
    private Label LblSupId;

    @FXML
    private TableView<SupplierTm> TblSupplier;

    @FXML
    private TextField TxtSupName;

    @FXML
    private TextField TxtContactNumber;

    @FXML
    private TextField TxtSearchSupplier;

    private final ObservableList<SupplierTm> supplierTms = FXCollections.observableArrayList();

    @FXML
    void BtnUpdateOnClickAction(ActionEvent event) throws SQLException {
        String supId = LblSupId.getText();
        String supName = TxtSupName.getText();
        String contactNumber = TxtContactNumber.getText();

        SupplierDto supplierDto = new SupplierDto(supId, supName, contactNumber);
        boolean isUpdated = supManageModel.updateSupplier(supplierDto);

        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update Supplier").show();
        }
    }

    @FXML
    void BtnAddOnClickAction(ActionEvent event) throws SQLException {
        String supId = LblSupId.getText();

        String supName = TxtSupName.getText();
        String contactNumber = TxtContactNumber.getText();

        SupplierDto supplierDto = new SupplierDto(supId, supName, contactNumber);
        boolean isSaved = supManageModel.saveSupplier(supplierDto);

        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save Supplier").show();
        }
    }


    @FXML
    void BtnDeleteOnClickAction(ActionEvent event) throws SQLException {
        String supId = LblSupId.getText();

        boolean isDeleted = supManageModel.deleteSupplier(supId);

        if (isDeleted) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete Supplier").show();
        }
    }


    @FXML
    void BtnGenerateRepOnClickAction(ActionEvent event) {

    }

    @FXML
    void BtnResetOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }


    @FXML
    void TblSupMouseOnClickAction(MouseEvent event) {
        SupplierTm supplierTm = TblSupplier.getSelectionModel().getSelectedItem();
        if (supplierTm != null) {
            LblSupId.setText(supplierTm.getSupId());
            TxtSupName.setText(supplierTm.getName());
            TxtContactNumber.setText(supplierTm.getContactNumber());

            BtnAddSup.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        ColSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Load SupplierId").show();
        }
        TblSupplier.setRowFactory(new Callback<TableView<SupplierTm>, TableRow<SupplierTm>>() {
            @Override
            public TableRow<SupplierTm> call(TableView<SupplierTm> tableView) {
                final TableRow<SupplierTm> row = new TableRow<>();

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

    SupManageModel supManageModel= new SupManageModel();
    SupplierTm supplierTm= new SupplierTm();
    private void refreshPage() throws SQLException {
        LblSupId.setText(supManageModel.getNextSupId());

        loadTableData();

        BtnAddSup.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtSupName.setText("");
        TxtContactNumber.setText("");

        supplierTms.clear();
        TblSupplier.refresh();
    }

    private void loadTableData() throws SQLException {
        ArrayList<SupplierDto> supplierDtos;

        supplierDtos = supManageModel.getAllSuppliers(); // Load all suppliers if no filter is applied

        ObservableList<SupplierTm> supplierTms1 = FXCollections.observableArrayList();

        for (SupplierDto supplierDto : supplierDtos) {
            SupplierTm supplierTm1 = new SupplierTm(
                    supplierDto.getSupId(),
                    supplierDto.getName(),
                    supplierDto.getContactNumber()
            );
            supplierTms1.add(supplierTm1);
        }

        TblSupplier.setItems(supplierTms1);
    }

    InvenManageModel invenManageModel = new InvenManageModel();

    @FXML
    void BtnSearchOnClickAction(ActionEvent event) throws SQLException {
        String searchText = TxtSearchSupplier.getText().trim();

        ArrayList<SupplierDto> searchResults = supManageModel.searchSuppliersByNameOrId(searchText);
        ObservableList<SupplierTm> supplierTms1 = FXCollections.observableArrayList();

        for (SupplierDto supplierDto : searchResults) {
            SupplierTm supplierTm = new SupplierTm(
                    supplierDto.getSupId(),
                    supplierDto.getName(),
                    supplierDto.getContactNumber()
            );
            supplierTms1.add(supplierTm);
        }

        TblSupplier.setItems(supplierTms1);
    }

}
