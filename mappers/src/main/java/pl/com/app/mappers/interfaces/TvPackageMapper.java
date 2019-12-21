package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.model.TvPackage;

@Mapper(componentModel = "spring")
public interface TvPackageMapper {
    @Mappings({
            @Mapping(source = "agreementDurations", target = "agreementDurationsDTO")
    })
    TvPackageDTO tvPackageToTvPackageDTO(TvPackage tvPackage);

    @Mappings({
            @Mapping(source = "agreementDurationsDTO", target = "agreementDurations")
    })
    TvPackage tvPackageDTOToTvPackage(TvPackageDTO tvPackageDTO);
}
