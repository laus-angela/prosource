package email.service.mailgun;

import email.gateway.MailGateway;
import email.gateway.MailRequest;
import email.service.EmailServiceProvider;
import email.service.Provider;
import email.web.api.model.EmailNotification;
import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailGunEmailService implements EmailServiceProvider {
    @Value("${email-gateway.mail-gun.api-key}")
    private String apiKey;

    @Value("${email-gateway.mail-gun.url}")
    private String gatewayUrl;

    private final MailGateway gateway;

    @Override
    public boolean isApplicable(String provider) {
        return provider.equalsIgnoreCase(Provider.MAILGUN.name());
    }

    @Override
    public Response sendEmail(EmailNotification emailNotification) {
        MailRequest request = MailRequest.builder()
                .uri(gatewayUrl)
                .apiKey(apiKey)
                .from(emailNotification.getSender())
                .to(emailNotification.getToRecipients())
                .subjectLine(emailNotification.getSubjectLine())
                .message(emailNotification.getMessage())
                .cc(emailNotification.getCcRecipients())
                .bcc(emailNotification.getBccRecipients())
                .build();

        emailNotification.setMessage(request.getMessage());

        // temporary static response
        return Optional.ofNullable(gateway.sendRequest(request))
                .map(email -> MailGunStaticApi.messages(emailNotification))
                .orElse(null);
    }
}
