package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.PackageDealDTO;
import pl.com.app.model.PackageDeal;

@Mapper(componentModel = "spring")
public interface PackageDealMapper {
    @Mappings({
            @Mapping(source = "tvPackage", target = "tvPackageDTO"),
            @Mapping(source = "agreement", target = "agreementDTO"),
            @Mapping(source = "agreement.user", target = "agreementDTO.userDTO")
    })
    PackageDealDTO packageDealToPackageDealDTO(PackageDeal packageDeal);

    @Mappings({
            @Mapping(source = "tvPackageDTO", target = "tvPackage"),
            @Mapping(source = "agreementDTO", target = "agreement"),
            @Mapping(source = "agreementDTO.userDTO", target = "agreement.user")
    })
    PackageDeal packageDealDTOToPackageDeal(PackageDealDTO packageDealDTO);
}