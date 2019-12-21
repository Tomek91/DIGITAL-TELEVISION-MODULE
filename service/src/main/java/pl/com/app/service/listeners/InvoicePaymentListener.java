package pl.com.app.service.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.com.app.dto.MailSenderDTO;
import pl.com.app.model.Invoice;
import pl.com.app.model.User;
import pl.com.app.service.InvoiceService;
import pl.com.app.service.MailService;

import java.util.UUID;

import static j2html.TagCreator.*;

@Component
@RequiredArgsConstructor
public class InvoicePaymentListener implements ApplicationListener<OnInvoicePaymentEvenData> {

    private final InvoiceService invoiceService;
    private final MailService mailService;

    @Override
    public void onApplicationEvent(OnInvoicePaymentEvenData data) {
        sendInvoicePaymentEmail(data);
    }

    private void sendInvoicePaymentEmail(OnInvoicePaymentEvenData data) {

        User user = data.getInvoice().getAgreement().getUser();
        Invoice invoice = data.getInvoice();
        String token = UUID.randomUUID().toString();
        invoiceService.createPaymentNumber(invoice, token);

        String recipientAddress = user.getEmail();
        String subject = "TV-APP: invoice payment confirmation";
        String url = data.getUrl() + "invoices/payment?token=" + token;


        String message =
                body(
                        h2("Hello!"),
                        h3("This is Tv-App."),
                        h3(
                                text("Click "),
                                a("here").withHref(url),
                                text(" to pay for invoice.")
                        )
                ).render();


        mailService.sendMail(
                MailSenderDTO.builder()
                        .recipientAddress(recipientAddress)
                        .subject(subject)
                        .message(message)
                        .build()
        );
    }
}
