package email.service;

import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.stereotype.Service;
import email.web.api.model.EmailNotification;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final List<EmailServiceProvider> emailServiceProviders;

    public Response sendNotification(EmailNotification emailNotification, String provider) {
        emailNotification.validate();
        return emailServiceProviders.stream()
                .filter(serviceProvider -> serviceProvider.isApplicable(provider))
                .map(serviceProvider -> serviceProvider.sendEmail(emailNotification))
                .findFirst().orElseThrow(() -> new UnableToSendNotificationException("No email service provider available."));
    }
}
