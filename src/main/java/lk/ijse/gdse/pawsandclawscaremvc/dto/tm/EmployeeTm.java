package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTm {
    private String empId;
    private String role;
    private String serviceId;
    private String startTime;
    private String contactNumber;
    private String endTime;
    private String orderId;
    private String employeeType;
}
