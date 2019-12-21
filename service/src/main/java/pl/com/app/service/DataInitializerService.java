package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.app.dto.*;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.*;
import pl.com.app.parsers.json.*;
import pl.com.app.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//@Transactional
@RequiredArgsConstructor
public class DataInitializerService {
    private final AgreementDurationRepository agreementDurationRepository;
    private final TvPackageRepository tvPackageRepository;
    private final AgreementDurationConverter agreementDurationConverter;
    private final TvPackagesConverter packagesConverter;
    private final UserRepository userRepository;
    private final AgreementRepository agreementRepository;
    private final PackageDealRepository packageDealRepository;
    private final RoleRepository roleRepository;
    private final PreferenceRepository preferenceRepository;
    private final InvoiceRepository invoiceRepository;
    private final AgreementsConverter agreementsConverter;
    private final DealsConverter dealsConverter;
    private final PreferencesConverter preferencesConverter;
    private final InvoicesConverter invoicesConverter;
    private final UsersConverter usersConverter;
    private final RolesConverter rolesConverter;
    private final AgreementMapper agreementMapper;
    private final PackageDealMapper packageDealMapper;
    private final InvoiceMapper invoiceMapper;
    private final PreferenceMapper preferenceMapper;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final TvPackageMapper tvPackageMapper;
    private final AgreementDurationMapper agreementDurationMapper;
    private final PasswordEncoder passwordEncoder;


    public InfoDTO initData() {
        try {
            preferenceRepository.deleteAll();
            invoiceRepository.deleteAll();
            packageDealRepository.deleteAll();
            agreementRepository.deleteAll();
            agreementDurationRepository.deleteAll();
            tvPackageRepository.deleteAll();
            userRepository.deleteAll();
            roleRepository.deleteAll();

            List<AgreementDurationDTO> agreementDurationDTOList = agreementDurationConverter.fromJson().orElseThrow(NullPointerException::new);
            List<TvPackageDTO> tvPackageDTOList = packagesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<UserDTO> userDTOList = usersConverter.fromJson().orElseThrow(NullPointerException::new);
            List<RoleDTO> roleDTOList = rolesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<AgreementDTO> agreementDTOList = agreementsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<PackageDealDTO> packageDealDTOList = dealsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<InvoiceDTO> invoiceDTOList = invoicesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<PreferenceDTO> preferenceDTOList = preferencesConverter.fromJson().orElseThrow(NullPointerException::new);

            roleRepository.saveAll(
                    roleDTOList
                            .stream()
                            .map(roleMapper::roleDTOToRole)
                            .collect(Collectors.toList())
            );

            userRepository.saveAll(
                    userDTOList
                            .stream()
                            .map(userMapper::userDTOToUser)
                            .peek(x -> x.setPassword(passwordEncoder.encode(x.getPassword())))
                            .peek(x -> x.setRole(roleRepository.findByName(x.getRole().getName()).orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND ROLE BY NAME EXCEPTION"))))
                            .collect(Collectors.toList())
            );

            agreementDurationRepository.saveAll(
                    agreementDurationDTOList
                            .stream()
                            .map(agreementDurationMapper::agreementDurationDTOToAgreementDuration)
                            .collect(Collectors.toList())
            );

            tvPackageRepository.saveAll(
                    tvPackageDTOList
                            .stream()
                            .map(tvPackageMapper::tvPackageDTOToTvPackage)
                            .peek(x -> x.setAgreementDurations(
                                    x.getAgreementDurations()
                                            .stream()
                                            .map(a -> agreementDurationRepository.findByDuration(a.getDuration()).orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND AGREEMENT BY DURATION EXCEPTION")))
                                            .collect(Collectors.toSet()))
                            )
                            .collect(Collectors.toList())
            );

            agreementRepository.saveAll(
                    agreementDTOList
                            .stream()
                            .map(agreementMapper::agreementDTOToAgreement)
                            .peek(x -> x.setUser(userRepository.findByUserName(x.getUser().getUserName()).orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND USER BY USERNAME EXCEPTION"))))
                            .collect(Collectors.toList())
            );

            preferenceRepository.saveAll(
                    preferenceDTOList
                            .stream()
                            .map(preferenceMapper::preferenceDTOToPreference)
                            .peek(x -> x.setUser(userRepository.findByUserName(x.getUser().getUserName()).orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND USER BY USERNAME EXCEPTION"))))
                            .collect(Collectors.toList())
            );

            packageDealRepository.saveAll(
                    packageDealDTOList
                            .stream()
                            .map(packageDealMapper::packageDealDTOToPackageDeal)
                            .peek(x -> x.setTvPackage(tvPackageRepository.findByName(x.getTvPackage().getName()).orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND TV PACKAGE BY NAME EXCEPTION"))))
                            .peek(x -> x.setAgreement(
                                    agreementRepository
                                            .findUserNewestAgreementByUsername(x.getAgreement().getUser().getUserName())
                                            .stream()
                                            .findFirst()
                                            .orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND AGREEMENT BY USERNAME EXCEPTION")))
                            )
                            .collect(Collectors.toList())
            );

            invoiceRepository.saveAll(
                    invoiceDTOList
                            .stream()
                            .map(invoiceMapper::invoiceDTOToInvoice)
                            .peek(x -> x.setAgreement(
                                    agreementRepository
                                            .findUserNewestAgreementByUsername(x.getAgreement().getUser().getUserName())
                                            .stream()
                                            .findFirst()
                                            .orElseThrow(() -> new NullPointerException("DATA INITIALIZER: FIND AGREEMENT BY USERNAME EXCEPTION")))
                            )
                            .collect(Collectors.toList())
            );


            return InfoDTO.builder().info("Init data OK.").build();

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
