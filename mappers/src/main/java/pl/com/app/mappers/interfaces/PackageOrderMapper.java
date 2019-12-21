package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.PackageOrderDTO;
import pl.com.app.model.PackageOrder;

@Mapper(componentModel = "spring")
public interface PackageOrderMapper {
    @Mappings({
            @Mapping(source = "tvPackage", target = "tvPackageDTO"),
            @Mapping(source = "user", target = "userDTO")
    })
    PackageOrderDTO packageOrderToPackageOrderDTO(PackageOrder packageOrder);

    @Mappings({
            @Mapping(source = "tvPackageDTO", target = "tvPackage"),
            @Mapping(source = "userDTO", target = "user")
    })
    PackageOrder packageOrderDTOToPackageOrder(PackageOrderDTO packageOrderDTO);
}
