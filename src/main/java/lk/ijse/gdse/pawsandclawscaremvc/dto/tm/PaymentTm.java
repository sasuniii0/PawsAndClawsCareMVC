package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTm {
    private String paymentId;
    private LocalDate paymentDate;
    private double paymentAmount;
    private String method;
    private String resId;
    private String orderId;
    private String custId;
    private String email;

}
