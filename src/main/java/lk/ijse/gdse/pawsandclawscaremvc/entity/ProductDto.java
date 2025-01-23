package lk.ijse.gdse.pawsandclawscaremvc.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ProductDto {
    private String productId;
    private String productName;
    private String description;
    private double price;
    private int qty;
}
