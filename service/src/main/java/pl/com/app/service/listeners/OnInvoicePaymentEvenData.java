package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.com.app.model.Invoice;

@Getter
public class OnInvoicePaymentEvenData extends ApplicationEvent {

    private final String url;
    private final Invoice invoice;

    public OnInvoicePaymentEvenData(String url, Invoice invoice) {
        super(invoice);
        this.url = url;
        this.invoice = invoice;
    }
}
