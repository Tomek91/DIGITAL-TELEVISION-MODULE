package pl.com.app.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.com.app.service.InvoiceService;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerateInvoicesJob {
    private final InvoiceService invoiceService;
    private final Environment environment;


    @Scheduled(cron = "0 0 10 10 * *")
    //@Scheduled(fixedDelay = 1000L)
    public void generateInvoices()  {
        invoiceService.generateInvoices();
    }
}
