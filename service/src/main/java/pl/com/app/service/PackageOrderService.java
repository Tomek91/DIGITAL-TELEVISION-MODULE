package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.PackageOrderDTO;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.AgreementMapper;
import pl.com.app.mappers.interfaces.PackageOrderMapper;
import pl.com.app.mappers.interfaces.TvPackageMapper;
import pl.com.app.mappers.interfaces.UserMapper;
import pl.com.app.model.*;
import pl.com.app.model.enums.OperationOrder;
import pl.com.app.repository.*;
import pl.com.app.service.listeners.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PackageOrderService {

    private final RestTemplate restTemplate;
    private final AgreementService agreementService;
    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;
    private final TvPackageMapper tvPackageMapper;
    private final TvPackageRepository tvPackageRepository;
    private final PackageOrderRepository packageOrderRepository;
    private final PackageOrderMapper packageOrderMapper;
    private final PackageDealRepository packageDealRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;


    public InfoDTO sendOrderConfirmationMail(Long id, UserDTO loggedUser, HttpServletRequest request) {

        try {
            if (id == null) {
                throw new NullPointerException("TV PACKAGE IS NULL");
            }
            if (loggedUser == null) {
                throw new NullPointerException("USER IS NULL");
            }
            if (loggedUser.getId() == null) {
                throw new NullPointerException("USER ID IS NULL");
            }

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnOrderPackageEvenData(url, userMapper.userDTOToUser(loggedUser), id));

            return InfoDTO.builder().info("Mail has been sent").build();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());

        }

    }
    public InfoDTO sendCancelConfirmationMail(Long id, UserDTO loggedUser, HttpServletRequest request) {

        try {
            if (id == null) {
                throw new NullPointerException("TV PACKAGE IS NULL");
            }
            if (loggedUser == null) {
                throw new NullPointerException("USER IS NULL");
            }
            if (loggedUser.getId() == null) {
                throw new NullPointerException("USER ID IS NULL");
            }

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnCancelPackageEvenData(url, userMapper.userDTOToUser(loggedUser), id));

            return InfoDTO.builder().info("Mail has been sent").build();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());

        }

    }

    public PackageOrderDTO confirmUserOrder(String token, OperationOrder operationOrder, Long packageId) {
        try {
            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            if (operationOrder == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            if (packageId == null) {
                throw new NullPointerException("PACKAGE ID IS NULL");
            }

            VerificationToken verificationToken
                    = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new NullPointerException("USER WITH TOKEN " + token + " DOESN'T EXIST"));

            if (verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                throw new NullPointerException("TOKEN HAS BEEN EXPIRED");
            }

            verificationToken.setToken(null);
            verificationTokenRepository.save(verificationToken);

            User user = verificationToken.getUser();

            TvPackage tvPackage = tvPackageRepository.findById(packageId).orElseThrow(NullPointerException::new);

            PackageOrder packageOrderToSave = PackageOrder.builder()
                    .user(user)
                    .operationOrder(operationOrder)
                    .tvPackage(tvPackage)
                    .orderDate(LocalDate.now())
                    .build();

            PackageOrder packageOrderFromDb = packageOrderRepository.save(packageOrderToSave);
            return packageOrderMapper.packageOrderToPackageOrderDTO(packageOrderFromDb);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public List<PackageOrderDTO> findAllNotConfirm() {
        try{
            return
                    packageOrderRepository.findAllNotConfirm()
                    .stream()
                    .map(packageOrderMapper::packageOrderToPackageOrderDTO)
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public PackageOrderDTO confirmOrder(Long id, TvPackageDTO packageDTO) {
        try {
            if (id == null) {
                throw new NullPointerException("ID IS NULL");
            }
            if (packageDTO == null) {
                throw new NullPointerException("PACKAGE IS NULL");
            }

            PackageOrder packageOrderToSave = packageOrderRepository.findById(id).orElseThrow(NullPointerException::new);
            packageOrderToSave.setIsCompleted(Boolean.TRUE);
            PackageOrder packageOrderFromDb = packageOrderRepository.save(packageOrderToSave);

            Agreement userNewestAgreement = agreementRepository
                    .findUserNewestAgreement(packageOrderToSave.getUser().getId())
                    .stream()
                    .findFirst()
                    .orElseThrow(NullPointerException::new);
            if (packageOrderToSave.getOperationOrder().equals(OperationOrder.ADD)) {

                PackageDeal packageDealToSave = PackageDeal.builder()
                        .tvPackage(tvPackageRepository.getOne(packageDTO.getId()))
                        .agreement(userNewestAgreement)
                        .build();

                packageDealRepository.save(packageDealToSave);
            } else {
                PackageDeal packageDealToDelete = packageDealRepository
                        .findByTvPackage_IdEqualsAndAgreement_IdEquals(packageDTO.getId(), userNewestAgreement.getId())
                        .orElseThrow(NullPointerException::new);

                packageDealRepository.delete(packageDealToDelete);
            }

            return packageOrderMapper.packageOrderToPackageOrderDTO(packageOrderFromDb);
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
