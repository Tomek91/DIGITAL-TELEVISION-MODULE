package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.app.dto.*;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.ParamsMapMapper;
import pl.com.app.mappers.interfaces.PreferenceMapper;
import pl.com.app.mappers.interfaces.TvPackageMapper;
import pl.com.app.model.Preference;
import pl.com.app.model.User;
import pl.com.app.repository.PreferenceRepository;
import pl.com.app.repository.TvPackageRepository;
import pl.com.app.repository.UserRepository;
import pl.com.app.service.restconfig.RestConfig;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {

    private final RestTemplate restTemplate;
    private final TvPackageRepository tvPackageRepository;
    private final TvPackageMapper tvPackageMapper;
    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final ParamsMapMapper paramsMapMapper;
    private final PreferenceMapper preferenceMapper;
    private final ChannelService channelService;


    public List<String> findAllCategoryPreferences() {
        try {
            ResponseEntity<ResponseMessage> responseEntity
                    = restTemplate.exchange(RestConfig.categoriessUrl, HttpMethod.GET, null, ResponseMessage.class);

            return paramsMapMapper
                    .paramsMapToCategoryDTOList(responseEntity.getBody())
                    .stream()
                    .map(CategoryDTO::getName)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PreferenceDTO> findAllUserCategoryPreferences(UserDTO loggedUser) {
        try {
            if (loggedUser == null) {
                throw new NullPointerException("LOGGED USER IS NULL");
            }

            return preferenceRepository
                    .findAllByUserId_Equals(loggedUser.getId())
                    .stream()
                    .map(preferenceMapper::preferenceToPreferenceDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PreferenceDTO> addCategoryPreferencesToUser(UserDTO loggedUser, List<String> preferenceCategories) {
        try {
            if (loggedUser == null) {
                throw new NullPointerException("LOGGED USER IS NULL");
            }
            if (preferenceCategories == null) {
                throw new NullPointerException("PREFERENCES CATEGORIES IS NULL");
            }

            User user = userRepository.findByUserName(loggedUser.getUserName()).orElseThrow(NullPointerException::new);
            List<Preference> preferencesToSave = preferenceCategories
                    .stream()
                    .map(categoryName -> Preference.builder().user(user).categoryName(categoryName).build())
                    .collect(Collectors.toList());

            List<Preference> preferencesFromDb = preferenceRepository.saveAll(preferencesToSave);
            return preferencesFromDb
                    .stream()
                    .map(preferenceMapper::preferenceToPreferenceDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public PreferenceDTO deleteCategoryPreferencesToUser(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("PREFERENCE ID IS NULL");
            }
            Preference preferenceToDelete = preferenceRepository.findById(id)
                    .orElseThrow(NullPointerException::new);

            preferenceRepository.delete(preferenceToDelete);
            return preferenceMapper.preferenceToPreferenceDTO(preferenceToDelete);


        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<TvPackageInfoDTO> findAllTvPackageByUserCategoryPreferences(UserDTO loggedUser) {
        try {
            if (loggedUser == null) {
                throw new NullPointerException("LOGGED USER IS NULL");
            }

            List<String> userPreferences = preferenceRepository
                    .findAllByUserId_Equals(loggedUser.getId())
                    .stream()
                    .map(Preference::getCategoryName)
                    .collect(Collectors.toList());

            List<ChannelDTO> channels = channelService.findAll();

            return tvPackageRepository
                    .findAllByNameIn(userPreferences)
                    .stream()
                    .map(tvPackageMapper::tvPackageToTvPackageDTO)
                    .map(x -> TvPackageInfoDTO
                            .builder()
                            .tvPackageDTO(x)
                            .channelsDTO(
                                    channels
                                    .stream()
                                    .filter(channel -> x.getName().equalsIgnoreCase(channel.getPackageName()))
                                    .collect(Collectors.toList())
                            )
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
