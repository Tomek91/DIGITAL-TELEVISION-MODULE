package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.AgreementOrderDTO;
import pl.com.app.model.AgreementOrder;

@Mapper(componentModel = "spring")
public interface AgreementOrderMapper {
    @Mappings({
            @Mapping(source = "user", target = "userDTO")
    })
    AgreementOrderDTO agreementOrderToAgreementOrderDTO(AgreementOrder agreementOrder);

    @Mappings({
            @Mapping(source = "userDTO", target = "user")
    })
    AgreementOrder agreementOrderDTOToAgreementOrder(AgreementOrderDTO agreementOrderDTO);
}
