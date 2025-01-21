package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.SupplierDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupManageModel {
    public String getNextSupId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select supId from Supplier order by supId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(3);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("SUP%03d", newIdIndex);
        }
        return "SUP001";
    }

    public ArrayList<SupplierDto> getAllSuppliers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Supplier");
        ArrayList<SupplierDto> supplierDto = new ArrayList<>();

        while (rst.next()) {
            SupplierDto supplierDtos = new SupplierDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            );
            supplierDto.add(supplierDtos);
        }
        return supplierDto;
    }

    public boolean saveSupplier(SupplierDto supplierDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO Supplier VALUES (?, ?, ?)",
                supplierDto.getSupId(),
                supplierDto.getName(),
                supplierDto.getContactNumber());
    }


    public boolean updateSupplier(SupplierDto supplierDto) throws SQLException {
        return CrudUtil.execute("UPDATE Supplier SET name = ?, contactNumber = ?  WHERE supId = ?",
                supplierDto.getName(),
                supplierDto.getContactNumber(),
                supplierDto.getSupId());
    }


    public boolean deleteSupplier(String supId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Supplier WHERE supId = ?", supId);
    }


    public ArrayList<SupplierDto> searchSuppliersByNameOrId(String searchText) throws SQLException {
        ArrayList<SupplierDto> suppliers = new ArrayList<>();
        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier WHERE supId LIKE ? OR name LIKE ?",
                "%" + searchText + "%", "%" + searchText + "%");

        while (rs.next()) {
            suppliers.add(new SupplierDto(
                    rs.getString("supId"),
                    rs.getString("name"),
                    rs.getString("contactNumber")
            ));
        }
        return suppliers;
    }

}
