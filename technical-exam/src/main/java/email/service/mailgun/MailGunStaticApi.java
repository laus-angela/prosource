package email.service.mailgun;

import email.web.api.model.EmailNotification;
import email.web.api.model.Response;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class MailGunStaticApi {
    public static Response messages(EmailNotification emailNotification) {
        return Response.builder()
                .headers("Mail Gun Email Service Provider - Sent")
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
