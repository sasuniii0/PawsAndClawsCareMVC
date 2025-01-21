package lk.ijse.gdse.pawsandclawscaremvc.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.pawsandclawscaremvc.dto.PaymentDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public ObservableList<String> getAllOrderIds() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT orderId FROM Orders");

        ObservableList<String> orderIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            orderIds.add(resultSet.getString(1));
        }
        return orderIds;
    }

    public ObservableList<String> getAllReservationIds() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT resId FROM Reservation");

        ObservableList<String> serviceIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            serviceIds.add(resultSet.getString(1));
        }
        return serviceIds;
    }

    public String getNextPaymentId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select paymentId from Payment order by paymentId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(3);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("PAY%03d", newIndex);
        }
        return "PAY001";
    }

    public ArrayList<PaymentDto> getAllPayments() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT p.paymentId,p.date,p.amount,p.method,p.resId,p.orderId,c.custId,c.email\n" +
                "    FROM Payment p LEFT JOIN Reservation r ON p.resId = r.resId LEFT JOIN Customer c On r.custId = c.custId");
        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();

        while (rst.next()) {
            PaymentDto paymentDto = new PaymentDto(
                    rst.getString(1),
                    rst.getDate(2).toLocalDate(),
                    rst.getDouble(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }

    public boolean savePayment(PaymentDto paymentDto) throws SQLException {
        CrudUtil.execute(
                "INSERT INTO Payment (paymentId, date,amount,method,resId,orderId) VALUES (?, ?, ?, ?, ?, ?)",
                paymentDto.getPaymentId(),
                paymentDto.getDate(),
                paymentDto.getAmount(),
                paymentDto.getMethod(),
                paymentDto.getResId(),
                paymentDto.getOrderId()

        );
        return true;
    }

    public void displayCustomerDetailsByResId(String newValue) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT c.custId, c.email FROM Customer c\n" +
                "JOIN Reservation r ON c.custId = r.custId\n" +
                "WHERE r.resId = ?", newValue);

        if (rst.next()) {
            rst.getString(1);
            rst.getString(2);
        }
    }

    public void displayCustomerDetailsByOrderId(String newValue) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT c.custId, c.email FROM Customer c JOIN Orders o ON c.custId = o.custId WHERE o.orderId = ?", newValue);
        if (rst.next()) {
            rst.getString(1);
            rst.getString(2);
        }
    }

    public boolean UpdatePayment(PaymentDto paymentDto) throws SQLException {
        return CrudUtil.execute(
                "update Payment set date=?, amount=?, method=?, resId=?, orderId=? where paymentId =?",
                paymentDto.getDate(),
                paymentDto.getAmount(),
                paymentDto.getMethod(),
                paymentDto.getResId(),
                paymentDto.getOrderId(),
                paymentDto.getPaymentId()
        );
    }

    public boolean deletePayment(String paymentIdTxt) throws SQLException {
        return CrudUtil.execute("delete from Payment where paymentId = ?",paymentIdTxt);
    }

    public ArrayList<PaymentDto> searchPaymentsByEmail(String searchText) throws SQLException {
        ResultSet rst = CrudUtil.execute("select p.paymentId ,p.date,p.amount,p.method,p.resId,p.orderId,c.custId,c.email\n" +
                "from Customer c Join Orders o on c.custId = o.custId\n" +
                "join Payment p on o.orderId = p.orderId\n" +
                "where c.email = ?", searchText);
        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();
        while (rst.next()) {
            PaymentDto paymentDto = new PaymentDto(
                    rst.getString(1),
                    rst.getDate(2).toLocalDate(),
                    rst.getDouble(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
            paymentDtos.add(paymentDto);
        }
        return paymentDtos;
    }
}
