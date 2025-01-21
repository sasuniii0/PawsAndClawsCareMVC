package lk.ijse.gdse.pawsandclawscaremvc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDto {
    private String customerId;
    private String customerName;
    private String address;
    private String email;
    private String contactNumber;
}
