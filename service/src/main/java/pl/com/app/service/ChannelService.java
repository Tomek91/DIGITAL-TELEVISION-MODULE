package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.app.dto.ChannelDTO;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.dto.TvPackageInfoDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.ParamsMapMapper;
import pl.com.app.mappers.interfaces.TvPackageMapper;
import pl.com.app.repository.TvPackageRepository;
import pl.com.app.service.restconfig.RestConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final RestTemplate restTemplate;
    private final TvPackageRepository tvPackageRepository;
    private final TvPackageMapper tvPackageMapper;
    private final ParamsMapMapper paramsMapMapper;


    @Cacheable(value = "tvpackagesinfo")
    public List<TvPackageInfoDTO> findAllGroupByTvPackage() {

        try {
            List<ChannelDTO> channelDTOList = findAll();

            List<TvPackageDTO> tvPackageList = tvPackageRepository
                    .findAll()
                    .stream()
                    .map(tvPackageMapper::tvPackageToTvPackageDTO)
                    .collect(Collectors.toList());

            List<TvPackageInfoDTO> packageInfoDTOList = channelDTOList
                    .stream()
                    .collect(Collectors.groupingBy(ChannelDTO::getPackageName))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> tvPackageList
                                    .stream()
                                    .filter(x -> x.getName().equalsIgnoreCase(e.getKey()))
                                    .findFirst()
                                    .orElseThrow(() -> new NullPointerException("TV PACKAGE NOT EXIST: " + e.getKey())),
                            Map.Entry::getValue
                    ))
                    .entrySet()
                    .stream()
                    .map(e -> TvPackageInfoDTO.builder().tvPackageDTO(e.getKey()).channelsDTO(e.getValue()).build())
                    .collect(Collectors.toList());

            return packageInfoDTOList;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }

    }

    public List<ChannelDTO> findAll() {
        try {
            ResponseEntity<ResponseMessage> responseEntity
                    = restTemplate.exchange(RestConfig.channelsUrl, HttpMethod.GET, null, ResponseMessage.class);

           return paramsMapMapper.paramsMapToChannelDTOList(responseEntity.getBody());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

}
