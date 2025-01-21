package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.OrderDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.ProductDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductManageModel {
    public static boolean saveProduct(ProductDto productDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO Product (proId, name, description,  price, qty) VALUES (?,?,?,?,?)",
                productDto.getProductId(),
                productDto.getProductName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getQty()

        );
    }

    public static boolean reduceQty(OrderDetailsDto orderDetailsDto) throws SQLException {
        return CrudUtil.execute(
                "update Product set qty = qty - ? where proId = ?",
                orderDetailsDto.getQuantity(),   // Quantity to reduce
                orderDetailsDto.getProId()      // Item ID
        );
    }

    public ArrayList<ProductDto> getAllProducts() throws SQLException {
        ResultSet rst = CrudUtil.execute("select proId,name,description,price,qty from Product");

        ArrayList<ProductDto> productDtos = new ArrayList<>();
        while (rst.next()){
            ProductDto productDto = new ProductDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public String getNextProductId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select proId from Product order by proId desc limit 1");
        if (rst.next()){
            String lastId = rst.getNString(1);
            String subString = lastId.substring(2);
            int i = Integer.parseInt(subString);
            int newIndex = i+1;
            return String.format("PR%03d", newIndex);
        }
        return "PR001";
    }

    public boolean deleteItem(String proId) throws SQLException {
        return CrudUtil.execute("delete from Product where proId = ?",proId);
    }

    public boolean updateProduct(ProductDto productDto) throws SQLException {
        return  CrudUtil.execute("update Product set name =?, description = ?, price = ?, qty = ? where proId = ?",
                productDto.getProductName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getQty(),
                productDto.getProductId()
        );
    }

    public ArrayList<String> getAllProductId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select proId from Product");
        ArrayList<String> proIds = new ArrayList<>();
        while (rst.next()) {
            proIds.add(rst.getString(1));
        }

        return proIds;
    }

    public ProductDto findById(String selectedProId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select proId,name,description,price,qty from Product where proId=?", selectedProId);

        if (rst.next()){
            ProductDto productDto = new ProductDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
            return productDto;
        }
        return null;
    }

    public ArrayList<ProductDto> searchProductsByCatalog(String searchText) throws SQLException {
        String query = "SELECT proId, name, description, price, qty FROM Product WHERE name LIKE ?";
        ResultSet rst = CrudUtil.execute(query, "%" + searchText + "%");

        ArrayList<ProductDto> productDtos = new ArrayList<>();
        while (rst.next()) {
            ProductDto productDto = new ProductDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
            productDtos.add(productDto);
        }
        return productDtos;
    }
    public ArrayList<ProductDto> getLowStockProducts() throws SQLException {
        String query = "SELECT proId, name, description, price, qty FROM Product WHERE qty < 10";
        ResultSet rst = CrudUtil.execute(query);

        ArrayList<ProductDto> lowStockProducts = new ArrayList<>();
        while (rst.next()) {
            ProductDto productDto = new ProductDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
            lowStockProducts.add(productDto);
        }
        return lowStockProducts;
    }
}
