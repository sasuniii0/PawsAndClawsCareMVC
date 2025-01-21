package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationTm {
    private String resId;
    private String custId;
    private String serviceId;
    private LocalDate date;
    private String description;
    private String dropOffTime;
    private String price;
    private double total;
    private Button removeBtn;
}
