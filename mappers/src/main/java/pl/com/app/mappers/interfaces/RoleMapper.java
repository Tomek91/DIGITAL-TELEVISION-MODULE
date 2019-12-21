package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import pl.com.app.dto.RoleDTO;
import pl.com.app.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);
}
