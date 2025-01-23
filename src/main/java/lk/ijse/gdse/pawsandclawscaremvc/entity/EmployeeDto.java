package lk.ijse.gdse.pawsandclawscaremvc.entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDto {
    private String empId;
    private String startTime;
    private String endTime;
    private String role;
    private String contactNumber;
    private String serviceId;
    private String orderId;
    private String employeeType;

}
