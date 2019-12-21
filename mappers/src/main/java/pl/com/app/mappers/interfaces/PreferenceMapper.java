package pl.com.app.mappers.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.com.app.dto.PreferenceDTO;
import pl.com.app.model.Preference;

@Mapper(componentModel = "spring")
public interface PreferenceMapper {
    @Mappings({
            @Mapping(source = "user", target = "userDTO")
    })
    PreferenceDTO preferenceToPreferenceDTO(Preference preference);

    @Mappings({
            @Mapping(source = "userDTO", target = "user")
    })
    Preference preferenceDTOToPreference(PreferenceDTO preferenceDTO);
}
