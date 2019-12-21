package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.com.app.model.User;

@Getter
public class OnCancelPackageEvenData extends ApplicationEvent {

    private final String url;
    private final User user;
    private final Long packageId;

    public OnCancelPackageEvenData(String url, User user, Long packageId) {
        super(user);
        this.url = url;
        this.user = user;
        this.packageId = packageId;
    }
}
