package pl.com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InvoiceDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal cost;
    private Month month;
    private Integer year;
    private LocalDate paidDate;
    private Boolean checkPaid;
    private String invoiceNumber;
    private AgreementDTO agreementDTO;

}
