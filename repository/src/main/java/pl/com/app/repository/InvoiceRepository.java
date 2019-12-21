package pl.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.com.app.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i JOIN i.agreement a JOIN a.user u WHERE u.id = :userId ORDER BY i.date DESC")
    List<Invoice> findUserInvoices(@Param("userId") Long userId);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM Invoice i where i.paidDate is not null and i.checkPaid <> true")
    List<Invoice> findAllToCheckPayment();
}
