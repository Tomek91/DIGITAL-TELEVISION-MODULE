package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.InvoiceDTO;
import pl.com.app.model.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mappings({
            @Mapping(source = "agreement", target = "agreementDTO"),
            @Mapping(source = "agreement.user", target = "agreementDTO.userDTO")
    })
    InvoiceDTO invoiceToInvoiceDTO(Invoice invoice);

    @Mappings({
            @Mapping(source = "agreementDTO", target = "agreement"),
            @Mapping(source = "agreementDTO.userDTO", target = "agreement.user")
    })
    Invoice invoiceDTOToInvoice(InvoiceDTO invoiceDTO);
}
