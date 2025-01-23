package lk.ijse.gdse.pawsandclawscaremvc.entity;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private String serviceId;
    private String description;
    private Time duration;
    private Double price;
    private String availability;
}
