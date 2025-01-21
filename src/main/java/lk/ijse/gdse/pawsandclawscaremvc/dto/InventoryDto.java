package lk.ijse.gdse.pawsandclawscaremvc.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
    private String inventoryId;
    private Date stockUpdate;
    private String inventoryCategory;
    private String availabilityStatus;
}
