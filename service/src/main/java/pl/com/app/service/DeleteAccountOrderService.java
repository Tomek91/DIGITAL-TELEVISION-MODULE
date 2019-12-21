package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.DeleteAccountOrderDTO;
import pl.com.app.dto.InfoDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.DeleteAccountMapper;
import pl.com.app.model.DeleteAccountOrder;
import pl.com.app.model.User;
import pl.com.app.model.VerificationToken;
import pl.com.app.repository.*;
import pl.com.app.service.listeners.*;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteAccountOrderService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final AgreementOrderRepository agreementOrderRepository;
    private final AgreementRepository agreementRepository;
    private final PackageOrderRepository packageOrderRepository;
    private final Environment environment;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final DeleteAccountMapper deleteAccountMapper;
    private final DeleteAccountOrderRepository deleteAccountOrderRepository;


    public DeleteAccountOrderDTO confirmDeleteAccountOrder(String token) {
        try {
            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            VerificationToken verificationToken
                    = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new NullPointerException("USER WITH TOKEN " + token + " DOESN'T EXIST"));

            if (verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                throw new NullPointerException("TOKEN HAS BEEN EXPIRED");
            }


            User user = verificationToken.getUser();

            DeleteAccountOrder deleteAccountOrderToSave = DeleteAccountOrder.builder()
                    .user(user)
                    .orderDate(LocalDate.now())
                    .build();

            verificationToken.setToken(null);
            verificationTokenRepository.save(verificationToken);

            DeleteAccountOrder deleteAccountOrderFromDb = deleteAccountOrderRepository.save(deleteAccountOrderToSave);
            return deleteAccountMapper.deleteAccountOrderToDeleteAccountOrderDTO(deleteAccountOrderFromDb);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public List<DeleteAccountOrderDTO> findAllNotConfirm() {
        try{
            return
                    deleteAccountOrderRepository.findAllNotConfirm()
                            .stream()
                            .map(deleteAccountMapper::deleteAccountOrderToDeleteAccountOrderDTO)
                            .collect(Collectors.toList());
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public InfoDTO confirmDeleteAccount(Long id, Map<String, Object> params) {
        try {
            if (id == null) {
                throw new NullPointerException("ID IS NULL");
            }
            if (params == null) {
                throw new NullPointerException("PARAMS IS NULL");
            }

            agreementOrderRepository.deleteAgreementOrderByUserId_Equals(id);
            packageOrderRepository.deleteAgreementOrderByUserId_Equals(id);
            agreementRepository.deleteAgreementOrderByUserId_Equals(id);
            verificationTokenRepository.deleteAgreementOrderByUserId_Equals(id);
            deleteAccountOrderRepository.deleteAgreementOrderByUserId_Equals(id);
            userRepository.deleteById(id);

            return InfoDTO.builder().info("Account has been deleted.").build();
        } catch (Exception e){
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public InfoDTO sendMailConfirmation(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("USER ID IS NULL");
            }
            User user = userRepository.findById(id).orElseThrow(NullPointerException::new);
            final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("local.server.port") + "/";
            eventPublisher.publishEvent(new OnDeleteAccountOrderEvenData(url, user));

            return InfoDTO.builder().info("Send mail to confirm delete account").build();
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}

