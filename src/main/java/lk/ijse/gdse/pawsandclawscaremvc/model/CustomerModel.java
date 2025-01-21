package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.CustomerDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO Customer (custId, name, address, email, contactNumber)\n" +
                        "VALUES (?, ?, ?, ?, ?)",
                customerDto.getCustomerId(),
                customerDto.getCustomerName(),
                customerDto.getAddress(),
                customerDto.getEmail(),
                customerDto.getContactNumber());
    }
    public String getNextCustomerId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select custId from Customer order by custId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("C%03d", newIndex);
        }
        return "C001";
    }

    public ArrayList<CustomerDto> getAllCustomers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Customer");
        ArrayList<CustomerDto> customerDtos = new ArrayList<>();

        while (rst.next()) {
            CustomerDto customerDto = new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            customerDtos.add(customerDto);
        }
        return customerDtos;
    }

    public boolean deleteCustomer(String customerId) throws SQLException {
        return CrudUtil.execute("delete from Customer where custId = ?",customerId);
    }

    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        return  CrudUtil.execute("update Customer set name=?, address =?, email=?, contactNumber=? where custId =?",
                customerDto.getCustomerName(),
                customerDto.getAddress(),
                customerDto.getEmail(),
                customerDto.getContactNumber(),
                customerDto.getCustomerId()
        );
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select custId from Customer");
        ArrayList<String> customerIds = new ArrayList<>();
        while (rst.next()) {
            customerIds.add(rst.getString(1));
        }
        return customerIds;
    }

    public CustomerDto findById(String selectedCustId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Customer where custId=?", selectedCustId);

        if (rst.next()) {
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }

    public CustomerDto SearchCustomerByContact(String contactNumber) {
        try{
            ResultSet rst = CrudUtil.execute("SELECT * FROM Customer WHERE contactNumber = ?",contactNumber);
            if (rst.next()){
                return new CustomerDto(
                        rst.getString("custId"),
                        rst.getString("name"),
                        rst.getString("address"),
                        rst.getString("email"),
                        rst.getString("contactNumber")
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCustomerNameById(String selectedCustId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT name FROM Customer WHERE custId = ?", selectedCustId);
        if (rst.next()) {
            return rst.getString(1);
        }
        return "Unknown Customer";
    }
}
