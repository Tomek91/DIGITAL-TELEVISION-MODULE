package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.enums.OperationOrder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageOrderDTO {
    private Long id;
    private OperationOrder operationOrder;
    private LocalDate orderDate;
    private Boolean isCompleted;
    private TvPackageDTO tvPackageDTO;
    private UserDTO userDTO;
}
