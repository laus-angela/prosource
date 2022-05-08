package email.service;

import email.exception.MailServicesOfflineException;
import email.web.api.model.EmailNotification;
import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final List<EmailServiceProvider> emailServiceProviders;

    public Response sendToProvider(EmailNotification emailNotification, String provider) {
        emailNotification.validate();
        return emailServiceProviders.stream()
                .filter(serviceProvider -> serviceProvider.isApplicable(provider))
                .map(serviceProvider -> serviceProvider.sendEmail(emailNotification))
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(() -> new MailServicesOfflineException("Mail service provider is offline"));
    }

    public Response send(EmailNotification emailNotification) {
        emailNotification.validate();
        return emailServiceProviders.stream()
                .map(serviceProvider -> serviceProvider.sendEmail(emailNotification))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new MailServicesOfflineException("No email service provider available."));
    }
}
