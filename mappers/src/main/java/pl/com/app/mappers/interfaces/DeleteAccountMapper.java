package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.DeleteAccountOrderDTO;
import pl.com.app.model.DeleteAccountOrder;

@Mapper(componentModel = "spring")
public interface DeleteAccountMapper {
    @Mappings({
            @Mapping(source = "user", target = "userDTO")
    })
    DeleteAccountOrderDTO deleteAccountOrderToDeleteAccountOrderDTO(DeleteAccountOrder deleteAccountOrder);

    @Mappings({
            @Mapping(source = "userDTO", target = "user")
    })
    DeleteAccountOrder deleteAccountOrderDTOTodeleteAccountOrder(DeleteAccountOrderDTO deleteAccountOrderDTO);
}
