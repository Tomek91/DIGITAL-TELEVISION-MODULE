package pl.com.app.service.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.com.app.dto.MailSenderDTO;
import pl.com.app.model.User;
import pl.com.app.service.MailService;
import pl.com.app.service.RegistrationService;

import java.util.UUID;

import static j2html.TagCreator.*;

@Component
@RequiredArgsConstructor
public class CancelPackageListener implements ApplicationListener<OnCancelPackageEvenData> {

    private final MailService mailService;
    private final RegistrationService registrationService;


    @Override
    public void onApplicationEvent(OnCancelPackageEvenData data) {
        sendOrderPackageEmail(data);
    }

    private void sendOrderPackageEmail(OnCancelPackageEvenData data) {

        User user = data.getUser();
        String token = UUID.randomUUID().toString();
        registrationService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "TV-APP: order package payment confirmation";
        String url = data.getUrl() + "package-orders/cancel/" + data.getPackageId() + "/confirmation?token=" + token;


        String message =
                body(
                        h2("Hello!"),
                        h3("This is Tv-App."),
                        h3(
                                text("Click "),
                                a("here").withHref(url),
                                text(" to confirm cancel package.")
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
