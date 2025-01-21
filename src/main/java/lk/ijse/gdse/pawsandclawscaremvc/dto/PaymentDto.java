package lk.ijse.gdse.pawsandclawscaremvc.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private String paymentId;
    private LocalDate date;
    private double amount;
    private String method;
    private String resId;
    private String orderId;
    private String custId;
    private String email;
}
