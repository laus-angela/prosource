package email.service.mailgun;

import email.service.EmailServiceProvider;
import email.service.Provider;
import email.web.api.model.Request;
import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import email.web.api.model.EmailNotification;

@Service
@RequiredArgsConstructor
public class MailGunEmailService implements EmailServiceProvider {

    @Value("${email-gateway.mail-gun.api-key}")
    private String apiKey;

    @Value("${email-gateway.mail-gun.url}")
    private String gatewayUrl;

    @Override
    public boolean isApplicable(String provider) {
        return provider.equalsIgnoreCase(Provider.MAILGUN.name());
    }

    @Override
    public Response sendEmail(EmailNotification emailNotification) {
        MailGun mailGunRequest = MailGun.builder()
                .apiKey(apiKey)
                .request(MailGunRequest.builder()
                        .method(HttpMethod.POST)
                        .endpoint(gatewayUrl)
                        .subject(emailNotification.getSubjectLine())
                        .from(emailNotification.getSender())
                        .text(emailNotification.getMessage())
                        .to(StringUtils.join(emailNotification.getToRecipients(), ';'))
                        .cc(StringUtils.join(emailNotification.getCcRecipients(), ';'))
                        .bcc(StringUtils.join(emailNotification.getBccRecipients(), ';'))
                        .build())
                .build();
        return MailGun.messages(mailGunRequest);
    }
}
