package lk.ijse.gdse.pawsandclawscaremvc.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartTm {
    private String proId;
    private String proName;
    private int cartQty;
    private double unitPrice;
    private double total;
    private Button removeBtn;
}
