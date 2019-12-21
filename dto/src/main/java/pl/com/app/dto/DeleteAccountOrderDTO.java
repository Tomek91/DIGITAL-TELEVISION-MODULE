package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteAccountOrderDTO {

    private Long id;
    private LocalDate orderDate;
    private Boolean isCompleted;
    private UserDTO userDTO;

}
