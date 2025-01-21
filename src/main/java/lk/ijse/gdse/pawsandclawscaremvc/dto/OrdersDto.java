package lk.ijse.gdse.pawsandclawscaremvc.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto {
    private String orderId;
    private String customerId;
    private Date orderDate;

    private ArrayList<OrderDetailsDto> orderDetailsDtos;
}
