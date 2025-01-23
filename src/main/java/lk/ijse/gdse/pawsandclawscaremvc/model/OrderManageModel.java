package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.db.DBConnection;
import lk.ijse.gdse.pawsandclawscaremvc.dto.OrderDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.OrdersDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderManageModel {
    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
    public String getNextOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select orderId from Orders order by orderId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("O%03d", newIdIndex);
        }
        return "O001";
    }

    public boolean saveOrder(OrdersDto ordersDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false); // 1

            boolean isOrderSaved = CrudUtil.execute(
                    "INSERT INTO Orders (orderId, custId, date) VALUES (?, ?, ?)",
                    ordersDto.getOrderId(),
                    ordersDto.getCustomerId(),
                    ordersDto.getOrderDate()
            );
            if (isOrderSaved) {
                boolean isOrderDetailListSaved = orderDetailsModel.saveOrderDetailsList(ordersDto.getOrderDetailsDtos());
                if (isOrderDetailListSaved) {
                    connection.commit(); // 2
                    return true;
                }
            }
            connection.rollback(); // 3
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true); // 4
        }
    }

    public String getOrderDateById(String selectedOrderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT date FROM Orders WHERE orderId = ?",selectedOrderId);
        while (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
