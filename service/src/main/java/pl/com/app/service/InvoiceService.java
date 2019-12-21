package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.app.dto.InvoiceDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.mappers.interfaces.InvoiceMapper;
import pl.com.app.model.Agreement;
import pl.com.app.model.Invoice;
import pl.com.app.model.PackageDeal;
import pl.com.app.model.User;
import pl.com.app.repository.AgreementRepository;
import pl.com.app.repository.InvoiceRepository;
import pl.com.app.repository.UserRepository;
import pl.com.app.service.listeners.*;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final AgreementRepository agreementRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment environment;

    public List<InvoiceDTO> findAllByUser(UserDTO loggedUser) {

        try {
            if (loggedUser == null) {
                throw new NullPointerException("USER IS NULL");
            }
            if (loggedUser.getId() == null) {
                throw new NullPointerException("USER ID IS NULL");
            }

            return invoiceRepository
                    .findUserInvoices(loggedUser.getId())
                    .stream()
                    .map(invoiceMapper::invoiceToInvoiceDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<InvoiceDTO> findAllToCheckPayment() {

        try {

            return invoiceRepository
                    .findAllToCheckPayment()
                    .stream()
                    .map(invoiceMapper::invoiceToInvoiceDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void lockUserIfNotPay() {
        try {
            List<Agreement> agreements = agreementRepository.findAllNotExpired();

            List<User> usersToBeLocked = agreements
                    .stream()
                    .filter(x ->
                            x.getInvoices()
                                    .stream()
                                    .anyMatch(invoice -> (invoice.getCheckPaid() == null || !invoice.getCheckPaid()) &&
                                            invoice.getDate().isBefore(LocalDate.now().minusMonths(2L)))
                    )
                    .map(Agreement::getUser)
                    .collect(Collectors.toList());

            List<User> usersToDb = usersToBeLocked
                    .stream()
                    .filter(x -> x.getLocked() == null || !x.getLocked())
                    .peek(x -> x.setLocked(Boolean.TRUE))
                    .collect(Collectors.toList());

            userRepository.saveAll(usersToDb);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void generateInvoices() {
        try {
            List<Agreement> agreements = agreementRepository.findAllNotExpired();

            Calendar now = Calendar.getInstance();
            List<Invoice> invoicesToSave = agreements
                    .stream()
                    .map(x -> Invoice
                            .builder()
                            .agreement(x)
                            .cost(calculateInvoiceCost(x.getPackageDeals()))
                            .date(LocalDate.now())
                            .month(Month.of(now.get(Calendar.MONTH) + 1))
                            .year(now.get(Calendar.YEAR))
                            .build())
                    .collect(Collectors.toList());

            invoiceRepository.saveAll(invoicesToSave);

            final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("local.server.port") + "/";// + request.getContextPath();
            invoicesToSave.forEach(invoice ->  eventPublisher.publishEvent(new OnInvoicePaymentEvenData(url, invoice)));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    private BigDecimal calculateInvoiceCost(Set<PackageDeal> packageDeals) {
        try {
            if (packageDeals == null) {
                throw new NullPointerException("PACKAGE DEALS IS NULL");
            }

            return packageDeals
                    .stream()
                    .map(x -> x.getTvPackage().getCost())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void createPaymentNumber(Invoice invoice, String token) {
        try {
            if (invoice == null) {
                throw new NullPointerException("INVOICE IS NULL");
            }

            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            invoice.setInvoiceNumber(token);
            invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }

    }

    public void checkPayment(String invoiceNumber) {
        try {
            if (invoiceNumber == null) {
                throw new NullPointerException("INVOICE NUMBER IS NULL");
            }

            Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                    .orElseThrow(() -> new NullPointerException("INVOICE WITH NUMBER " + invoiceNumber + " DOESN'T EXIST"));

            invoice.setPaidDate(LocalDate.now());
            invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public InvoiceDTO setPaymentStatus(Long id, Map<String, Object> params) {
        try {
            if (id == null) {
                throw new NullPointerException("id is null");
            }

            if (params == null) {
                throw new NullPointerException("params is null");
            }

            if (params.get("checkPaid") == null) {
                throw new NullPointerException("params checkPaid is null");
            }

            Invoice invoice = invoiceRepository.findById(id).orElseThrow(NullPointerException::new);
            invoice.setCheckPaid(params.get("checkPaid") == null ? invoice.getCheckPaid() : Boolean.valueOf(params.get("checkPaid").toString()));

            Invoice invoiceAfterUpdate = invoiceRepository.save(invoice);
            return invoiceMapper.invoiceToInvoiceDTO(invoiceAfterUpdate);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
