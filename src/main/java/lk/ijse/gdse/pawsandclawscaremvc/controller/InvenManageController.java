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
import lk.ijse.gdse.pawsandclawscaremvc.dto.InventoryDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.tm.InventoryTm;
import lk.ijse.gdse.pawsandclawscaremvc.model.InvenManageModel;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InvenManageController implements Initializable {

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateInvenRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnSearch;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<InventoryTm, String> ColCategory;

    @FXML
    private TableColumn<InventoryTm, String> ColInvenId;

    @FXML
    private TableColumn<InventoryTm, Integer> ColStock;

    @FXML
    private TableColumn<InventoryTm, Date> ColStockUpdate;

    @FXML
    private Label LblInvenId;

    @FXML
    private TableView<InventoryTm> TblInventory;

    @FXML
    private TextField TxtCategory;

    @FXML
    private TextField TxtSearchCategory;

    @FXML
    private TextField TxtStock;

    @FXML
    private DatePicker TxtStockUpdateDate;

    @FXML
    void BtnSearchOnClickAction(ActionEvent event) {
        String searchText = TxtSearchCategory.getText().toLowerCase();  // Get the text from the search field

        try {
            // Call the method to search for products by catalog
            ArrayList<InventoryDto> filteredProducts = invenManageModel.searchProductsByCatalog(searchText);

            // Convert the filtered products to ProductTm objects
            ObservableList<InventoryTm> filteredList = FXCollections.observableArrayList();
            for (InventoryDto inventoryDto : filteredProducts) {
                InventoryTm inventoryTm = new InventoryTm(
                        inventoryDto.getInventoryId(),
                        inventoryDto.getStockUpdate(),
                        inventoryDto.getInventoryCategory(),
                        inventoryDto.getAvailabilityStatus()
                );
                filteredList.add(inventoryTm);
            }

            // Update the TableView with the filtered list
            TblInventory.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while searching Inventory", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteBtnOnClickAction(ActionEvent event) throws SQLException {
        String invenId = LblInvenId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = invenManageModel.deleteItem(invenId);
            if (isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Inventory Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to delete Inventory...").show();
            }
        }
    }

    @FXML
    void ResetBtnOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void SaveBtnOnClickAction(ActionEvent event) throws SQLException {
        String inventoryId = LblInvenId.getText();
        Date stockUpdate = Date.valueOf(TxtStockUpdateDate.getValue());
        String category = TxtCategory.getText();
        String status = TxtStock.getText();

        TxtStockUpdateDate.setStyle(TxtStockUpdateDate.getStyle() + " -fx-border-color: blue;");
        TxtCategory.setStyle(TxtCategory.getStyle() + " -fx-border-color: blue;");
        TxtStock.setStyle(TxtCategory.getStyle() + " -fx-border-color: blue;");

        InventoryDto inventoryDto = new InventoryDto(inventoryId,stockUpdate,category,status);
        boolean isSaved = invenManageModel.saveInventory(inventoryDto);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Saved", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK).show();
        }
    }

    @FXML
    void TblOnClickAction(MouseEvent event) {
        InventoryTm inventoryTm =TblInventory.getSelectionModel().getSelectedItem();
        if (inventoryTm != null) {
            LblInvenId.setText(inventoryTm.getInventoryId());
            TxtStockUpdateDate.setValue(inventoryTm.getStockUpdate().toLocalDate());  // Directly convert java.sql.Date to LocalDate
            TxtCategory.setText(inventoryTm.getInventoryCategory());
            TxtStock.setText(String.valueOf(inventoryTm.getAvailabilityStatus()));

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @FXML
    void UpdateBtnOnClickAction(ActionEvent event) throws SQLException {
        String inventoryId = LblInvenId.getText();
        Date stockUpdate = Date.valueOf(TxtStockUpdateDate.getValue());
        String category = TxtCategory.getText();
        String status = TxtStock.getText();

        TxtStockUpdateDate.setStyle(TxtStockUpdateDate.getStyle() + " -fx-border-color: blue;");
        TxtCategory.setStyle(TxtCategory.getStyle() + " -fx-border-color: blue;");
        TxtStock.setStyle(TxtCategory.getStyle() + " -fx-border-color: blue;");

        InventoryDto inventoryDto = new InventoryDto(inventoryId,stockUpdate,category,status);
        boolean isSaved = invenManageModel.updateInventory(inventoryDto);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Updated", ButtonType.OK).show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK).show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColInvenId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        ColStockUpdate.setCellValueFactory(new PropertyValueFactory<>("stockUpdate"));
        ColCategory.setCellValueFactory(new PropertyValueFactory<>("inventoryCategory"));
        ColStock.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Load Inventory Id").show();
        }
        TblInventory.setRowFactory(new Callback<TableView<InventoryTm>, TableRow<InventoryTm>>() {
            @Override
            public TableRow<InventoryTm> call(TableView<InventoryTm> tableView) {
                final TableRow<InventoryTm> row = new TableRow<>();

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
        loadNextInventoryId();
        loadTableData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtStockUpdateDate.setValue(null);
        TxtCategory.setText("");
        TxtStock.setText("");
    }

    private void loadTableData() throws SQLException {
        ArrayList<InventoryDto> inventoryDtos = invenManageModel.getAllInventory();

        ObservableList<InventoryTm> inventoryTms = FXCollections.observableArrayList();

        for (InventoryDto inventoryDto : inventoryDtos) {
            InventoryTm inventoryTm = new InventoryTm(
                    inventoryDto.getInventoryId(),
                    inventoryDto.getStockUpdate(),
                    inventoryDto.getInventoryCategory(),
                    inventoryDto.getAvailabilityStatus()
            );
            inventoryTms.add(inventoryTm);
        }
        TblInventory.setItems(inventoryTms);
    }

    InvenManageModel invenManageModel = new InvenManageModel();

    private void loadNextInventoryId() throws SQLException {
        String nextInventoryId = invenManageModel.getNextInventoryId();
        LblInvenId.setText(nextInventoryId);
    }
}
