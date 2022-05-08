package email.service;

import email.gateway.MailGateway;
import email.gateway.MailRequest;
import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import email.web.api.model.EmailNotification;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultEmailService implements EmailServiceProvider {
    private final MailGateway gateway;

    @Override
    public boolean isApplicable(String provider) {
        return Arrays.stream(Provider.values())
                .collect(Collectors.toList()).stream()
                .filter(p -> p.name().equalsIgnoreCase(provider))
                .findFirst().isEmpty();
    }

    @Override
    public Response sendEmail(EmailNotification emailNotification) {
        MailRequest request = MailRequest.builder()
                .uri("default-gateway-uri")
                .apiKey("default-api-key")
                .from(emailNotification.getSender())
                .to(emailNotification.getToRecipients())
                .subjectLine(emailNotification.getSubjectLine())
                .message(emailNotification.getMessage())
                .cc(emailNotification.getCcRecipients())
                .bcc(emailNotification.getBccRecipients())
                .build();

        emailNotification.setMessage(request.getMessage());

        return Response.builder()
                .headers("Default Email Service Provider - Sent")
                .subjectLine(emailNotification.getSubjectLine())
                .sender(emailNotification.getSender())
                .body(emailNotification.getMessage())
                .recipients(StringUtils.join(emailNotification.getToRecipients(), ';'))
                .hasCc(Optional.ofNullable(emailNotification.getCcRecipients()).isPresent())
                .hasBcc(Optional.ofNullable(emailNotification.getBccRecipients()).isPresent())
                .statusCode(HttpStatus.OK)
                .build();
    }
}
