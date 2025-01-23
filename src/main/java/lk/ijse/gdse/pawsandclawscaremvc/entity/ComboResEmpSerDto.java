package lk.ijse.gdse.pawsandclawscaremvc.entity;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComboResEmpSerDto {
    private String empRole;
    private String serviceId;
    private String serviceDesc;
    private Date reservationDate;
    private Time schedule;
}
