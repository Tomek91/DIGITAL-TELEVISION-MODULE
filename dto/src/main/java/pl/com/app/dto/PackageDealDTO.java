package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PackageDealDTO {
    private Long id;
    private TvPackageDTO tvPackageDTO;
    private AgreementDTO agreementDTO;



}
