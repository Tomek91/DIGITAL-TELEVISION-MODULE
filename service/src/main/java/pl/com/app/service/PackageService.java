package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.TvPackageMapper;
import pl.com.app.model.PackageDeal;
import pl.com.app.repository.AgreementRepository;
import pl.com.app.repository.TvPackageRepository;
import pl.com.app.service.restconfig.RestConfig;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageService {

    private final RestTemplate restTemplate;
    private final AgreementRepository agreementRepository;
    private final TvPackageMapper tvPackageMapper;
    private final TvPackageRepository tvPackageRepository;


    @Cacheable(value = "tvpackages")
    public List<TvPackageDTO> findAll() {

        try {
            ResponseEntity<ResponseMessage> responseEntity
                    = restTemplate.exchange(RestConfig.channelsUrl, HttpMethod.GET, null, ResponseMessage.class);

            return ((ResponseMessage<List<TvPackageDTO>>) responseEntity.getBody()).getData();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<TvPackageDTO> findAllUserPackages(UserDTO loggedUser) {
        try {
            if (loggedUser == null) {
                throw new NullPointerException("USER IS NULL");
            }
            if (loggedUser.getId() == null) {
                throw new NullPointerException("USER ID IS NULL");
            }

            return agreementRepository
                    .findUserNewestAgreement(loggedUser.getId())
                    .stream()
                    .findFirst()
                    .stream()
//                    .map(Stream::of)
//                    .orElseGet(Stream::empty)
                    .flatMap(x -> x.getPackageDeals().stream())
                    .map(PackageDeal::getTvPackage)
                    .map(tvPackageMapper::tvPackageToTvPackageDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());

        }
    }

    public List<TvPackageDTO> findAllNotUserPackages(UserDTO loggedUser) {
        try {
            if (loggedUser == null) {
                throw new NullPointerException("USER IS NULL");
            }
            if (loggedUser.getId() == null) {
                throw new NullPointerException("USER ID IS NULL");
            }

            List<TvPackageDTO> userPackages = findAllUserPackages(loggedUser);
            return tvPackageRepository.findAll()
                    .stream()
                    .filter(x -> !userPackages.contains(x))
                    .map(tvPackageMapper::tvPackageToTvPackageDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());

        }
    }

}
