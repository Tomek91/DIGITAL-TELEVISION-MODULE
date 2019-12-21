package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.AgreementDTO;
import pl.com.app.dto.AgreementOrderDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.AgreementMapper;
import pl.com.app.mappers.interfaces.AgreementOrderMapper;
import pl.com.app.mappers.interfaces.PackageDealMapper;
import pl.com.app.mappers.interfaces.UserMapper;
import pl.com.app.model.*;
import pl.com.app.model.enums.EAgreementOrder;
import pl.com.app.repository.AgreementOrderRepository;
import pl.com.app.repository.AgreementRepository;
import pl.com.app.repository.PackageDealRepository;
import pl.com.app.repository.VerificationTokenRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgreementOrderService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final AgreementOrderMapper agreementOrderMapper;
    private final AgreementMapper agreementMapper;
    private final AgreementOrderRepository agreementOrderRepository;
    private final AgreementService agreementService;
    private final AgreementRepository agreementRepository;
    private final PackageDealRepository packageDealRepository;
    private final PackageDealMapper packageDealMapper;
    private final UserMapper userMapper;


    public AgreementOrderDTO confirmAgreementOrder(String token, EAgreementOrder eAgreementOrder) {
        try {
            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            if (eAgreementOrder == null) {
                throw new NullPointerException("AGREEMENT ORDER IS NULL");
            }

            VerificationToken verificationToken
                    = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new NullPointerException("USER WITH TOKEN " + token + " DOESN'T EXIST"));

            if (verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                throw new NullPointerException("TOKEN HAS BEEN EXPIRED");
            }


            User user = verificationToken.getUser();

            AgreementOrder agreementOrderToSave = AgreementOrder.builder()
                    .user(user)
                    .agreementOrder(eAgreementOrder)
                    .orderDate(LocalDate.now())
                    .build();

            verificationToken.setToken(null);
            verificationTokenRepository.save(verificationToken);

            AgreementOrder agreementOrderFromDb = agreementOrderRepository.save(agreementOrderToSave);
            return agreementOrderMapper.agreementOrderToAgreementOrderDTO(agreementOrderFromDb);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public List<AgreementOrderDTO> findAllNotConfirm() {
        try{
            return
                    agreementOrderRepository.findAllNotConfirm()
                            .stream()
                            .map(agreementOrderMapper::agreementOrderToAgreementOrderDTO)
                            .collect(Collectors.toList());
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public AgreementOrderDTO confirmOrder(Long id, Map<String, Object> params) {
        try {
            if (id == null) {
                throw new NullPointerException("ID IS NULL");
            }
            if (params == null) {
                throw new NullPointerException("PARAMS IS NULL");
            }

            if (params.get("checkAgreement") == null) {
                throw new NullPointerException("params checkAgreement is null");
            }

            AgreementOrder agreementOrderToSave = agreementOrderRepository.findById(id).orElseThrow(NullPointerException::new);
            agreementOrderToSave.setIsCompleted(Boolean.TRUE);
            AgreementOrder agreementOrderFromDb = agreementOrderRepository.save(agreementOrderToSave);

            UserDTO userDTO = userMapper.userToUserDTO(agreementOrderToSave.getUser());
            Agreement userNewestAgreement = agreementRepository
                    .findUserNewestAgreement(userDTO.getId())
                    .stream()
                    .findFirst()
                    .orElseThrow(NullPointerException::new);

            if (agreementOrderToSave.getAgreementOrder().equals(EAgreementOrder.EXTEND)) {
                Period periodDiff = Period.between ( userNewestAgreement.getStartDate() , userNewestAgreement.getEndDate() );
                LocalDate endDate = LocalDate.now().plusDays(periodDiff.getDays());
                AgreementDTO agreementDTOToSave = AgreementDTO.builder()
                        .startDate(LocalDate.now())
                        .endDate(endDate)
                        .userDTO(userDTO)
                        .build();

                AgreementDTO agreementDTOFromDb = agreementService.add(agreementDTOToSave);
                Agreement agreementFromDb = agreementMapper.agreementDTOToAgreement(agreementDTOFromDb);

                List<PackageDeal> packageDealsToSave = userNewestAgreement
                        .getPackageDeals()
                        .stream()
                        .map(x -> PackageDeal.builder().agreement(agreementFromDb).tvPackage(x.getTvPackage()).build())
                        .collect(Collectors.toList());

                packageDealRepository.saveAll(packageDealsToSave);
            }

            return agreementOrderMapper.agreementOrderToAgreementOrderDTO(agreementOrderFromDb);
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}

