package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;
import pl.com.app.model.User;

@Getter
public class OnFileAttachEvenData extends ApplicationEvent {

    private final String url;
    private final User user;
    private final MultipartFile[] uploadedFiles;

    public OnFileAttachEvenData(String url, User user, MultipartFile[] uploadedFiles) {
        super(user);
        this.url = url;
        this.user = user;
        this.uploadedFiles = uploadedFiles;
    }
}
