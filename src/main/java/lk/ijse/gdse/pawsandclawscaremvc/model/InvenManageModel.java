package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.InventoryDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvenManageModel {

    public String getNextInventoryId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select inventoryId from Inventory order by inventoryId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("I%03d", newIndex);
        }
        return "I001";
    }

    public ArrayList<InventoryDto> getAllInventory() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Inventory");
        ArrayList<InventoryDto> inventoryDtos = new ArrayList<>();

        while (rst.next()) {
            InventoryDto inventoryDto = new InventoryDto(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getString(3),
                   rst.getString(4)
            );
            inventoryDtos.add(inventoryDto);
        }
        return inventoryDtos;
    }

    public boolean saveInventory(InventoryDto inventoryDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO Inventory (inventoryId, stockUpdate, inventoryCategory,availabilityStatus) VALUES (?,?,?,?)",
                inventoryDto.getInventoryId(),
                inventoryDto.getStockUpdate(),
                inventoryDto.getInventoryCategory(),
                inventoryDto.getAvailabilityStatus()
        );
    }

    public boolean updateInventory(InventoryDto inventoryDto) throws SQLException {
        return  CrudUtil.execute("update Inventory set stockUpdate =?, inventoryCategory = ?, availabilityStatus = ? where inventoryId = ?",
                inventoryDto.getStockUpdate(),
                inventoryDto.getInventoryCategory(),
                inventoryDto.getAvailabilityStatus(),
                inventoryDto.getInventoryId()
        );
    }

    public boolean deleteItem(String invenId) throws SQLException {
        return CrudUtil.execute("delete from Inventory where inventoryId = ?",invenId);

    }

    public ArrayList<InventoryDto> searchProductsByCatalog(String searchText) throws SQLException {
        String query = "SELECT inventoryId, stockUpdate, inventoryCategory, availabilityStatus FROM Inventory WHERE inventoryCategory LIKE ?";
        ResultSet rst = CrudUtil.execute(query, "%" + searchText + "%");

        ArrayList<InventoryDto> inventoryDtos = new ArrayList<>();
        while (rst.next()) {
            InventoryDto inventoryDto = new InventoryDto(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getString(3),
                   rst.getString(4)
            );
            inventoryDtos.add(inventoryDto);
        }
        return inventoryDtos;
    }

    public ArrayList<String> getAllInventoryId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select inventoryId from Inventory");
        ArrayList<String> invenIds = new ArrayList<>();
        while (rst.next()) {
            invenIds.add(rst.getString(1));
        }

        return invenIds;
    }

    public InventoryDto findById(String selectedInvenId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Inventory where inventoryId=?", selectedInvenId);

        if (rst.next()) {
            return new InventoryDto(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }
        return null;
    }
}
