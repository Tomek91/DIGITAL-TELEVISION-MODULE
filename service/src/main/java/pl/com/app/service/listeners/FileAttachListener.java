package pl.com.app.service.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.com.app.dto.MailSenderDTO;
import pl.com.app.model.User;
import pl.com.app.service.MailService;

import static j2html.TagCreator.*;

@Component
@RequiredArgsConstructor
public class FileAttachListener implements ApplicationListener<OnFileAttachEvenData> {

    private final MailService mailService;

    @Override
    public void onApplicationEvent(OnFileAttachEvenData data) {
        sendFileAttachEmail(data);
    }

    private void sendFileAttachEmail(OnFileAttachEvenData data) {

        User user = data.getUser();

        String recipientAddress = user.getEmail();
        String subject = "TV-APP: advertisements";


        String message =
                body(
                        h2("Hello!"),
                        h3("This is Tv-App."),
                        h3(
                                text("We send you advertisements.")
                        )
                ).render();


        mailService.sendMail(
                MailSenderDTO.builder()
                        .recipientAddress(recipientAddress)
                        .subject(subject)
                        .message(message)
                        .attachments(data.getUploadedFiles())
                        .build()
        );
    }
}
