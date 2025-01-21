package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import lombok.*;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceDetailsTm {
    private String resId;
    private String serviceId;
    private Date date;
    private String description;
    private String dropOffTime;
    private String pickUpTime;
    private double price;
}
