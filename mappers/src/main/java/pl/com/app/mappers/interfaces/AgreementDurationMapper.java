package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.AgreementDurationDTO;
import pl.com.app.model.AgreementDuration;

@Mapper(componentModel = "spring")
public interface AgreementDurationMapper {
    @Mappings({
            @Mapping(source = "tvPackages", target = "tvPackagesDTO")
    })
    AgreementDurationDTO agreementDurationToAgreementDurationDTO(AgreementDuration agreementDuration);

    @Mappings({
            @Mapping(source = "tvPackagesDTO", target = "tvPackages")
    })
    AgreementDuration agreementDurationDTOToAgreementDuration(AgreementDurationDTO agreementDurationDTO);
}
