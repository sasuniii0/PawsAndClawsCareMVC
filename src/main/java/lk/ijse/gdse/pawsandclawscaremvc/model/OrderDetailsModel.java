package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.OrderDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsModel {
    public boolean saveOrderDetailsList(ArrayList<OrderDetailsDto> orderDetailsDtos) throws SQLException {
        for (OrderDetailsDto orderDetailsDto : orderDetailsDtos) {
            // @isOrderDetailsSaved: Saves the individual order detail
            boolean isOrderDetailsSaved = saveOrderDetail(orderDetailsDto);
            if (!isOrderDetailsSaved) {
                // Return false if saving any order detail fails
                return false;
            }

            // @isItemUpdated: Updates the item quantity in the stock for the corresponding order detail
            boolean isItemUpdated = ProductManageModel.reduceQty(orderDetailsDto);
            if (!isItemUpdated) {
                // Return false if updating the item quantity fails
                return false;
            }
        }
        // Return true if all order details are saved and item quantities updated successfully
        return true;
    }

    private boolean saveOrderDetail(OrderDetailsDto orderDetailsDto) throws SQLException {
        return CrudUtil.execute(
                "insert into OrderDetails values (?,?,?,?)",
                orderDetailsDto.getOrderId(),
                orderDetailsDto.getProId(),
                orderDetailsDto.getQuantity(),
                orderDetailsDto.getPrice()
        );
    }
}
