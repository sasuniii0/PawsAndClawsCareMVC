package lk.ijse.gdse.pawsandclawscaremvc.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailsDto {
    private String orderId;
    private String proId;
    private int quantity;
    private double price;
}
