package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class CustomerTm {
    private String customerId;
    private String customerName;
    private String address;
    private String email;
    private String contactNumber;
}