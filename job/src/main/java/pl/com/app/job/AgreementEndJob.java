package pl.com.app.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.com.app.service.AgreementService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AgreementEndJob {
    private final AgreementService agreementService;
    private final Environment environment;


    @Scheduled(cron = "0 0 9 * * *")
    public void checkInvoices() {
        agreementService.findEndedAndSendMail();
    }
}
