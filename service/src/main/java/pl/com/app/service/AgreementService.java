package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.AgreementDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.AgreementMapper;
import pl.com.app.model.Agreement;
import pl.com.app.model.User;
import pl.com.app.repository.AgreementRepository;
import pl.com.app.repository.UserRepository;
import pl.com.app.service.listeners.*;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgreementService {
    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment environment;
    private final UserRepository userRepository;



    public List<AgreementDTO> findAll() {

        try {

            return agreementRepository
                    .findAll()
                    .stream()
                    .map(agreementMapper::agreementToAgreementDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public AgreementDTO findUserPackages(UserDTO loggedUser) {

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
                    .map(agreementMapper::agreementToAgreementDTO)
                    .orElseThrow(NullPointerException::new);


        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void findEndedAndSendMail() {
        try {
            final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("local.server.port") + "/";
            agreementRepository.findAllEnded()
                    .forEach(x -> eventPublisher.publishEvent(new OnOrderAgreementEvenData(url, x.getUser())));

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public AgreementDTO add(AgreementDTO agreementDTO) {
        try {
            if (agreementDTO == null){
                throw new NullPointerException("AGREEMENT IS NULL");
            }
            if (agreementDTO.getUserDTO() == null){
                throw new NullPointerException("USER IS NULL");
            }
            if (agreementDTO.getUserDTO().getId() == null){
                throw new NullPointerException("USER ID IS NULL");
            }
            User user = userRepository.findById(agreementDTO.getUserDTO().getId()).orElseThrow(NullPointerException::new);

            Agreement agreementToSave = agreementMapper.agreementDTOToAgreement(agreementDTO);
            agreementToSave.setUser(user);

            Agreement agreementFromDb = agreementRepository.save(agreementToSave);
            return agreementMapper.agreementToAgreementDTO(agreementFromDb);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }



}

