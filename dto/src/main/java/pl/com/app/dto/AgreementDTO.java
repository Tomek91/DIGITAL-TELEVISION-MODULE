package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AgreementDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserDTO userDTO;
    private Set<PackageDealDTO> packageDealsDTO;
}
