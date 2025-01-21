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
import lk.ijse.gdse.pawsandclawscaremvc.dto.PetDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.tm.PetTm;
import lk.ijse.gdse.pawsandclawscaremvc.model.PetManageModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PetManageController implements Initializable {

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
    private Button BtnUpdate;

    @FXML
    private TableColumn<PetTm, String> ColBreed;

    @FXML
    private TableColumn<PetTm, String> ColName;

    @FXML
    private TableColumn<PetTm, String> ColPetId;

    @FXML
    private Label LblPetId;

    @FXML
    private TableView<PetTm> TblPet;

    @FXML
    private TextField TxtBreed;

    @FXML
    private TextField TxtName;

    @FXML
    private TextField TxtSearchPetName;

    private final ObservableList<PetTm> petTms = FXCollections.observableArrayList();

    @FXML
    void BtnSearchOnClickAction(ActionEvent event) throws SQLException {
        String searchText = TxtSearchPetName.getText().trim();

        ArrayList<PetDto> searchResults = petManageModel.searchPetsByNameOrId(searchText);
        ObservableList<PetTm> petTms = FXCollections.observableArrayList();

        for (PetDto petDto : searchResults) {
            PetTm petTm = new PetTm(
                    petDto.getPetId(),
                    petDto.getName(),
                    petDto.getBreed()
            );
            petTms.add(petTm);
        }

        TblPet.setItems(petTms);
    }

    @FXML
    void DeleteBtnOnClickAction(ActionEvent event) throws SQLException {
        String petId = LblPetId.getText();

        boolean isDeleted = petManageModel.deletePet(petId);

        if (isDeleted) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Pet Deleted").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete Pet").show();
        }
    }

    @FXML
    void GenerateRepClickOnAction(ActionEvent event) {

    }

    @FXML
    void ResetBtnOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void SaveBtnOnClickAction(ActionEvent event) throws SQLException {
        String petId = LblPetId.getText();
        String name = TxtName.getText();
        String breed = TxtBreed.getText();

        PetDto petDto = new PetDto(petId, name, breed);
        boolean isSaved = petManageModel.savePet(petDto);

        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Pet Saved").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save Pet").show();
        }
    }

    @FXML
    void TblOnClick(MouseEvent event) {
       PetTm petTm = TblPet.getSelectionModel().getSelectedItem();
        if (petTm != null) {
            LblPetId.setText(petTm.getPetId());
            TxtName.setText(petTm.getName());
            TxtBreed.setText(petTm.getBreed());

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @FXML
    void UpdateBtnOnClickAction(ActionEvent event) throws SQLException {
        String petId = LblPetId.getText();
        String name = TxtName.getText();
        String breed = TxtBreed.getText();

        PetDto petDto = new PetDto(petId, name, breed);
        boolean isUpdated = petManageModel.updatePet(petDto);

        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Pet Updated").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update Pet").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColPetId.setCellValueFactory(new PropertyValueFactory<>("petId"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColBreed.setCellValueFactory(new PropertyValueFactory<>("breed"));

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Load SupplierId").show();
        }
        TblPet.setRowFactory(new Callback<TableView<PetTm>, TableRow<PetTm>>() {
            @Override
            public TableRow<PetTm> call(TableView<PetTm> tableView) {
                final TableRow<PetTm> row = new TableRow<>();

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

    PetManageModel petManageModel = new PetManageModel();
    private void refreshPage() throws SQLException {
        LblPetId.setText(petManageModel.getNextPetId());

        loadTableData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtName.setText("");
        TxtBreed.setText("");

        petTms.clear();
        TblPet.refresh();
    }

    private void loadTableData() throws SQLException {
        ArrayList<PetDto> petDtos;

        petDtos = petManageModel.getAllPets();

        ObservableList<PetTm> petTms = FXCollections.observableArrayList();

        for (PetDto petDto : petDtos) {
            PetTm petTm = new PetTm(
                    petDto.getPetId(),
                    petDto.getName(),
                    petDto.getBreed()
            );
            petTms.add(petTm);
        }

        TblPet.setItems(petTms);
    }
}
