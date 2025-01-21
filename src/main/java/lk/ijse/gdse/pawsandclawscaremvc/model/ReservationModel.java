package lk.ijse.gdse.pawsandclawscaremvc.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.pawsandclawscaremvc.db.DBConnection;
import lk.ijse.gdse.pawsandclawscaremvc.dto.ReservationDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationModel {

    public String getNextReservationId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select resId from Reservation order by resId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i + 1;
            return String.format("R%03d", newIndex);
        }
        return "R001";
    }

    public ObservableList<String> getAllCustomerId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT custId FROM Customer");

        ObservableList<String> customerIdList = FXCollections.observableArrayList();
        while (rs.next()) {
            customerIdList.add(rs.getString("custId"));
        }
        return customerIdList;
    }

    public boolean checkServiceAvailability(String services, String date, String dropOffTime) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Reservation r join ServiceDetails s on r.resId = s.resId\n" +
                "WHERE s.serviceId = ? AND r.date = ? AND r.dropOffTime = ?;",date,services,dropOffTime);

       return rst.next();
    }

    public ArrayList<String> getAvailableEmployee() throws SQLException {
        ArrayList<String> availableEmployees = new ArrayList<>();
        ResultSet rst = CrudUtil.execute(
                "select empId,roll from Employee"
        );
        while (rst.next()) {
            String empId = rst.getString(1);
            String roll = rst.getString(2);
            availableEmployees.add(empId + " " + roll);
        }
        return availableEmployees;

    }

    public String getSelectedServicePrice(String selectedService) throws SQLException {
        ResultSet rst = CrudUtil.execute("select price from Service where serviceId = ?",selectedService);
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    ServiceDetailsModel serviceDetailsModel = new ServiceDetailsModel();

    public boolean saveReservation(ReservationDto reservationDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try{
            connection.setAutoCommit(false);
            boolean isReservationSaved = CrudUtil.execute("insert into Reservation (resId,dropOffTime,custId,date) values (?,?,?,?)",
                    reservationDto.getResId(),
                    reservationDto.getDropOffTime(),
                    reservationDto.getCustId(),
                    reservationDto.getDate()
                    );
            if (isReservationSaved) {
                boolean isServiceDetailsSaved = serviceDetailsModel.saveReservationDetailsList(reservationDto.getServiceDetailsDtos());
                System.out.println("detail table " + isServiceDetailsSaved);

                if (isServiceDetailsSaved) {
                    connection.commit();
                    return true;
                }
            }
            System.out.println("res table " + isReservationSaved);
            connection.rollback();
            return false;
        }catch(SQLException e){
            e.printStackTrace();
            connection.rollback();
            return false;
        }finally{
            connection.setAutoCommit(true);
        }
    }
}
