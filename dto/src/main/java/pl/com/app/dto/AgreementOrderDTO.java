package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.enums.EAgreementOrder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgreementOrderDTO {
    private Long id;
    private EAgreementOrder EAgreementOrder;
    private LocalDate orderDate;
    private Boolean isCompleted;
    private UserDTO userDTO;

}
