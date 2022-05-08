package email.service.sendgrid;

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
public class SendGridEmailService implements EmailServiceProvider {
    @Value("${email-gateway.send-grid.api-key}")
    private String apiKey;

    @Value("${email-gateway.send-grid.url}")
    private String gatewayUrl;

    private final MailGateway gateway;

    @Override
    public boolean isApplicable(String provider) {
        return provider.equalsIgnoreCase(Provider.SENDGRID.name());
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
                .map(email -> SendGridStaticAPI.api(emailNotification))
                .orElse(null);
    }
}
