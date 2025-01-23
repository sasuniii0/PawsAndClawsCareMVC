package lk.ijse.gdse.pawsandclawscaremvc.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
