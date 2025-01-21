package lk.ijse.gdse.pawsandclawscaremvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import lk.ijse.gdse.pawsandclawscaremvc.db.DBConnection;
import lk.ijse.gdse.pawsandclawscaremvc.dto.CustomerDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.OrderDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.OrdersDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.ProductDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.tm.CartTm;
import lk.ijse.gdse.pawsandclawscaremvc.model.CustomerModel;
import lk.ijse.gdse.pawsandclawscaremvc.model.OrderManageModel;
import lk.ijse.gdse.pawsandclawscaremvc.model.ProductManageModel;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderManageController implements Initializable {

    @FXML
    private Button BtnAddToCart;

    @FXML
    private Button BtnGenerateRep;

    @FXML
    private Button BtnPlaceOrder;

    @FXML
    private Button BtnReset;

    @FXML
    private ComboBox<String> CmbCustomer;

    @FXML
    private ComboBox<String> CmbProduct;

    @FXML
    private TableColumn<?, ?> ColAction;

    @FXML
    private TableColumn<CartTm, String> ColProId;

    @FXML
    private TableColumn<CartTm, String> ColProName;

    @FXML
    private TableColumn<CartTm, Integer> ColQty;

    @FXML
    private TableColumn<CartTm, Double> ColTotal;

    @FXML
    private TableColumn<CartTm, Double> ColUnitPrice;

    @FXML
    private Label LblCustomerName;

    @FXML
    private Label LblProductName;

    @FXML
    private Label LblOrderDate;

    @FXML
    private Label LblOrderId;

    @FXML
    private Label LblQtyStock;

    @FXML
    private Label LlblUnitPrice;

    @FXML
    private Label LblDesc;

    @FXML
    private TableView<CartTm> TblOrders;

    @FXML
    private TextField TxtQty;

    private final OrderManageModel orderManageModel = new OrderManageModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final ProductManageModel productManageModel = new ProductManageModel();

    private final ObservableList<CartTm> cartTms = FXCollections.observableArrayList();

    @FXML
    void AddToCartOnClickAction(ActionEvent event) {
        String selectedProId = CmbProduct.getValue();
        if (selectedProId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select Product..!").show();
            return;
        }

        String cartQtyString = TxtQty.getText();
        String qtyPattern = "^[0-9]+$";

//        String pricePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
//        right :- 500.00. 500.65, 500,
//        wrong :- 787.8, 6777.9999

        if (!cartQtyString.matches(qtyPattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid quantity..!").show();
            return;
        }

        String proName = LblProductName.getText();
        int cartQty = Integer.parseInt(cartQtyString);
        int qtyOnHand = Integer.parseInt(LblQtyStock.getText());


        if (qtyOnHand < cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough Products..!").show();
            return;
        }

        TxtQty.setText("");

        double unitPrice = Double.parseDouble(LlblUnitPrice.getText());
        double total = unitPrice * cartQty;


        for (CartTm cartTm : cartTms) {
            if (cartTm.getProId().equals(selectedProId)) {
                int newQty = cartTm.getCartQty() + cartQty;
                cartTm.setCartQty(newQty);
                cartTm.setTotal(unitPrice * newQty);
                TblOrders.refresh();
                return;
            }
        }
        Button btn = new Button("Remove");
        CartTm newCartTm = new CartTm(
                selectedProId,
                proName,
                cartQty,
                unitPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            cartTms.remove(newCartTm);
            TblOrders.refresh();
        });
        cartTms.add(newCartTm);
    }

    @FXML
    void CmbCustomerOnClickAction(ActionEvent event) throws SQLException {
        String selectedCustId = CmbCustomer.getSelectionModel().getSelectedItem();
        CustomerDto customerDto = customerModel.findById(selectedCustId);

        if (customerDto != null) {
            LblCustomerName.setText(customerDto.getCustomerName());
        }
    }

    @FXML
    void CmbProductOnClickAction(ActionEvent event) throws SQLException {
        String selectedProId = CmbProduct.getSelectionModel().getSelectedItem();
        ProductDto productDto = productManageModel.findById(selectedProId);

        if (productDto != null) {
            LblProductName.setText(productDto.getProductName());
            LblQtyStock.setText(String.valueOf(productDto.getQty()));
            LlblUnitPrice.setText(String.valueOf(productDto.getPrice()));
            LblDesc.setText(productDto.getDescription());
        }
    }

    @FXML
    void GenerateRepBtnOnClickAction(ActionEvent event) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/Reports/CustomerAllOrderReports.jrxml"
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
    void PlaceOrderBtnOnClickAction(ActionEvent event) throws SQLException {
        if (TblOrders.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add Products to cart..!").show();
            return;
        }
        if (CmbProduct.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select customer for place order..!").show();
            return;
        }

        String orderId = LblOrderId.getText();
        Date dateOfOrder = Date.valueOf(LblOrderDate.getText());
        String customerId = CmbCustomer.getValue();

        // List to hold order details
        ArrayList<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();

        // Collect data for each item in the cart and add to order details array
        for (CartTm cartTm : cartTms) {

            // Create order details for each cart item
            OrderDetailsDto orderDetailsDto = new OrderDetailsDto(
                    orderId,
                    cartTm.getProId(),
                    cartTm.getCartQty(),
                    cartTm.getUnitPrice()
            );
            orderDetailsDtos.add(orderDetailsDto);
        }
        OrdersDto ordersDto = new OrdersDto(
                orderId,
                customerId,
                dateOfOrder,
                orderDetailsDtos
        );

        boolean isSaved = orderManageModel.saveOrder(ordersDto);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Order saved..!").show();

            // Reset the page after placing the order
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order fail..!").show();
        }
    }

    @FXML
    void ResetBtnOnClickAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void onClickTableOrders(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try{
            refreshPage();
        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }

    private void refreshPage() throws SQLException {
        LblOrderId.setText(orderManageModel.getNextOrderId());
        LblOrderDate.setText(LocalDate.now().toString());
        loadCustomerIds();
        loadProductIds();

        CmbCustomer.getSelectionModel().clearSelection();
        CmbProduct.getSelectionModel().clearSelection();
        LblProductName.setText("");
        LblQtyStock.setText("");
        LlblUnitPrice.setText("");
        TxtQty.setText("");
        LblCustomerName.setText("");

        cartTms.clear();

        TblOrders.refresh();
    }

    private void loadProductIds() throws SQLException {
        ArrayList<String> itemIds = productManageModel.getAllProductId();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        CmbProduct.setItems(observableList);
    }

    private void loadCustomerIds() throws SQLException {
        ArrayList<String> customerIds = customerModel.getAllCustomerIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIds);
        CmbCustomer.setItems(observableList);
    }

    private void setCellValues() {
        ColProId.setCellValueFactory(new PropertyValueFactory<>("proId"));
        ColProName.setCellValueFactory(new PropertyValueFactory<>("proName"));
        ColQty.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        ColUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        ColTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        ColAction.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        TblOrders.setItems(cartTms);

    }
}
