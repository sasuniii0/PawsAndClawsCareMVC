package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import lk.ijse.gdse.pawsandclawscaremvc.db.DBConnection;
import lk.ijse.gdse.pawsandclawscaremvc.dto.ProductDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.tm.ProductTm;
import lk.ijse.gdse.pawsandclawscaremvc.model.ProductManageModel;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProductManageController implements Initializable {

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnGenerateAllProductsRep11;

    @FXML
    private Button BtnSearch;

    @FXML
    private TextField TxtSearchProduct;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnReset;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnUpdate;

    @FXML
    private TableColumn<ProductTm, String> ColDesc;

    @FXML
    private TableColumn<ProductTm, Double> ColPrice;

    @FXML
    private TableColumn<ProductTm, String> ColProId;

    @FXML
    private TableColumn<ProductTm, String> ColProName;

    @FXML
    private TableColumn<ProductTm, Integer> ColQty;

    @FXML
    private Label LblDesc;

    @FXML
    private Label LblPrice;

    @FXML
    private Label LblProId;

    @FXML
    private Label LblProName;

    @FXML
    private Label LblQty;

    @FXML
    private TextField TxtDesc;

    @FXML
    private TextField TxtPrice;

    @FXML
    private TextField TxtProName;

    @FXML
    private TextField TxtQty;

    @FXML
    private TableView<ProductTm> TblProduct;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ObservableList<ProductTm> productList = FXCollections.observableArrayList();
    ProductManageModel prm = new ProductManageModel();


    @FXML
    void DeleteOnClickAction(ActionEvent event) throws SQLException {
        String proId = LblProId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            boolean isDeleted = productManageModel.deleteItem(proId);
            if (isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION,"Product Deleted").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to delete Product...").show();
            }
        }
    }

    @FXML
    void GenerateAllProductRepOnClickAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/Reports/Product.jrxml"
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

    @FXML
    void SaveOnClickAction(ActionEvent event) throws SQLException {
        String proId = LblProId.getText();
        String proName = TxtProName.getText();
        String desc = TxtDesc.getText();
        double price = Double.parseDouble(TxtPrice.getText());
        int qty = Integer.parseInt(TxtQty.getText());

        ProductDto productDto = new ProductDto(proId, proName,desc, price,qty);
        boolean isSaved = productManageModel.saveProduct(productDto);
        if (isSaved){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Product Saved").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to save Product").show();
        }
    }

    @FXML
    void TblProductOnClick(MouseEvent event) {
        ProductTm productTm =TblProduct.getSelectionModel().getSelectedItem();
        if (productTm != null) {
            LblProId.setText(productTm.getProductId());
            TxtProName.setText(productTm.getProductName());
            TxtDesc.setText(productTm.getDescription());
            TxtPrice.setText(String.valueOf(productTm.getPrice()));
            TxtQty.setText(String.valueOf(productTm.getQty()));

            BtnSave.setDisable(true);
            BtnDelete.setDisable(false);
            BtnUpdate.setDisable(false);
        }
    }

    @FXML
    void UpdateOnClickAction(ActionEvent event) throws SQLException {
        String proId = LblProId.getText();
        String proName = TxtProName.getText();
        String desc = TxtDesc.getText();
        double price = Double.parseDouble(TxtPrice.getText());
        int qty = Integer.parseInt(TxtQty.getText());

        ProductDto productDto = new ProductDto(proId, proName,desc, price,qty);

        boolean isUpdated = productManageModel.updateProduct(productDto);
        if (isUpdated){
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Product Updated").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to update Product").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ColProId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        ColProName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        ColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        ColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        ColQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        scheduler.scheduleAtFixedRate(this::checkLowQuantityProducts, 0, 24, TimeUnit.HOURS);

        try{
            refreshPage();
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to load ProductId",ButtonType.OK);
            alert.showAndWait();
        }
        TblProduct.setRowFactory(new Callback<TableView<ProductTm>, TableRow<ProductTm>>() {
            @Override
            public TableRow<ProductTm> call(TableView<ProductTm> productTmTableView) {
                final TableRow<ProductTm> row = new TableRow<>();

                row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        row.setStyle("-fx-background-color: #00d2d3;");
                    }else{
                        row.setStyle("-fx-background-color: #ffffff;");
                    }
                });
                return row;
            }
        });
    }

    private void refreshPage() throws SQLException {
        loadNextProductId();
        loadTableData();

        BtnSave.setDisable(false);
        BtnDelete.setDisable(true);
        BtnUpdate.setDisable(true);

        TxtProName.setText("");
        TxtDesc.setText("");
        TxtPrice.setText("");
        TxtQty.setText("");
    }

    ProductManageModel productManageModel = new ProductManageModel();

    private void loadTableData() throws SQLException {
        ArrayList<ProductDto> productsDtos = productManageModel.getAllProducts();
        ObservableList<ProductTm> productTms = FXCollections.observableArrayList();

        for (ProductDto productDto : productsDtos) {
            ProductTm productTm = new ProductTm(
                    productDto.getProductId(),
                    productDto.getProductName(),
                    productDto.getDescription(),
                    productDto.getPrice(),
                    productDto.getQty()
            );
            productTms.add(productTm);
        }
        TblProduct.setItems(productTms);
    }

    private void loadNextProductId() throws SQLException {
        String nextProductId = productManageModel.getNextProductId();
        LblProId.setText(nextProductId);
    }

    @FXML
    void BtnSearchOnClickAction(ActionEvent event) {
            String searchText = TxtSearchProduct.getText().toLowerCase();  // Get the text from the search field

            try {
                // Call the method to search for products by catalog
                ArrayList<ProductDto> filteredProducts = productManageModel.searchProductsByCatalog(searchText);

                // Convert the filtered products to ProductTm objects
                ObservableList<ProductTm> filteredList = FXCollections.observableArrayList();
                for (ProductDto productDto : filteredProducts) {
                    ProductTm productTm = new ProductTm(
                            productDto.getProductId(),
                            productDto.getProductName(),
                            productDto.getDescription(),
                            productDto.getPrice(),
                            productDto.getQty()
                    );
                    filteredList.add(productTm);
                }

                // Update the TableView with the filtered list
                TblProduct.setItems(filteredList);

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error while searching products", ButtonType.OK);
                alert.showAndWait();
            }
    }
    private void checkLowQuantityProducts() {
        Platform.runLater(() -> {
            try {
                List<ProductDto> lowStockProducts = productManageModel.getLowStockProducts();
                if (!lowStockProducts.isEmpty()) {
                    displayReminder(lowStockProducts);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private void displayReminder(List<ProductDto> lowStockProducts) {
        Stage reminderStage = new Stage();
        reminderStage.initStyle(StageStyle.TRANSPARENT);
        reminderStage.setAlwaysOnTop(true);

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-background-color: #e66767; -fx-padding: 10;");

        Label title = new Label("Low Stock Alert");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e272e;");

        vbox.getChildren().add(title);
        for (ProductDto product : lowStockProducts) {
            Label productInfo = new Label(product.getProductName() + " - Qty: " + product.getQty());
            vbox.getChildren().add(productInfo);
        }

        Scene scene = new Scene(vbox);
        reminderStage.setScene(scene);

        // Position the alert in the bottom-right corner
        reminderStage.setX(Screen.getPrimary().getVisualBounds().getMaxX() - 300);
        reminderStage.setY(Screen.getPrimary().getVisualBounds().getMaxY() - 200);

        // Position the alert in the top-right corner
        reminderStage.setX(Screen.getPrimary().getVisualBounds().getMaxX() - 300);
        reminderStage.setY(Screen.getPrimary().getVisualBounds().getMinY() + 20);

        reminderStage.show();

        // Auto-close after 10 seconds
        new Timeline(new KeyFrame(Duration.seconds(10), e -> reminderStage.close())).play();
    }
}
