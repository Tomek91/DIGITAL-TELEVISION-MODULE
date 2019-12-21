package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgreementDurationDTO {
    private Long id;
    private Integer duration;
    private Set<TvPackageDTO> tvPackagesDTO;
}
