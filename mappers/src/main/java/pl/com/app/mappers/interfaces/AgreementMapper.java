package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.AgreementDTO;
import pl.com.app.model.Agreement;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mappings({
            @Mapping(source = "user", target = "userDTO"),
            @Mapping(source = "packageDeals", target = "packageDealsDTO")

    })
    AgreementDTO agreementToAgreementDTO(Agreement agreement);

    @Mappings({
            @Mapping(source = "userDTO", target = "user"),
            @Mapping(source = "packageDealsDTO", target = "packageDeals")
    })
    Agreement agreementDTOToAgreement(AgreementDTO agreementDTO);
}
