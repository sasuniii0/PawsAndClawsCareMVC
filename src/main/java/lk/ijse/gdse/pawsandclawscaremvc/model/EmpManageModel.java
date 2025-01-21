package lk.ijse.gdse.pawsandclawscaremvc.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.pawsandclawscaremvc.dto.EmployeeDto ;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmpManageModel {
    public String getNextEmpId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select empId from Employee order by empId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("E%03d", newIndex);
        }
        return "E001";
    }

    public ArrayList<EmployeeDto> getAllEmployees() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Employee");
        ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();

        while (rst.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    rst.getString("empId"),
                    rst.getString("orderId"),
                    rst.getString("employeeType"),
                    rst.getString("roll"),
                    rst.getString("serviceId"),
                    rst.getString("startTime"),
                    rst.getString("contactNumber"),
                    rst.getString("endTime")
            );
            employeeDtos.add(employeeDto);
        }
        return employeeDtos;
    }

    public ObservableList<String> getAllServiceIds() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT serviceId FROM Service");

        ObservableList<String> serviceIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            serviceIds.add(resultSet.getString(1));
        }
        return serviceIds;
        
    }

    public String getServiceDescription(String newValue) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT description FROM Service WHERE serviceId = ?", newValue);

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }

    public ObservableList<String> getAllOrderIds() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT orderId FROM Orders");

        ObservableList<String> orderIds = FXCollections.observableArrayList();
        while (resultSet.next()) {
            orderIds.add(resultSet.getString(1));
        }
        return orderIds;
    }

    public Object getOrderDate(String newValue) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT orderDate FROM Orders WHERE orderId = ?", newValue);

        if (resultSet.next()) {
            return resultSet.getDate(1);
        }
        return null;
    }

    public boolean saveEmployee(EmployeeDto employeeDto) throws SQLException {;
        System.out.println(employeeDto);
        CrudUtil.execute(
                "INSERT INTO Employee (empId, roll,contactNumber, serviceId, orderId, employeeType, endTime, startTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                employeeDto.getEmpId(),
                employeeDto.getRole(),
                employeeDto.getContactNumber(),
                employeeDto.getServiceId(),
                employeeDto.getOrderId(),
                employeeDto.getEmployeeType(),
                employeeDto.getEndTime(),
                employeeDto.getStartTime()
        );
        return true;
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws SQLException {
        return CrudUtil.execute(
                "update Employee set employeeType=?, startTime = ?, endTime=?, roll=?, contactNumber=?, serviceId=?, orderId =? where empId =?",
                employeeDto.getEmployeeType(),
                employeeDto.getStartTime(),
                employeeDto.getEndTime(),
                employeeDto.getRole(),
                employeeDto.getContactNumber(),
                employeeDto.getServiceId(),
                employeeDto.getOrderId(),
                employeeDto.getEmpId()
        );
    }

    public boolean deleteService(String serviceIdText) throws SQLException {
        return CrudUtil.execute("delete from Service where empId = ?",serviceIdText);
    }

    public ArrayList<EmployeeDto> searchEmployeeByRole(String searchText) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT empId, roll, contactNumber, serviceId, orderId,employeeType,startTime, endTime FROM Employee WHERE roll LIKE ?", "%" + searchText + "%");

        ArrayList<EmployeeDto> employeeDtos = new ArrayList<>();
        while (rst.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
            employeeDtos.add(employeeDto);
        }
        return employeeDtos;
    }
}
