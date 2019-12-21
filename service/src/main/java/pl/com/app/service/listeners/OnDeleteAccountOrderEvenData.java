package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.com.app.model.User;

@Getter
public class OnDeleteAccountOrderEvenData extends ApplicationEvent {

    private final String url;
    private final User user;

    public OnDeleteAccountOrderEvenData(String url, User user) {
        super(user);
        this.url = url;
        this.user = user;
    }
}
