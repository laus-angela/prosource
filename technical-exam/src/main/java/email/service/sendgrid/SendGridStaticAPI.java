package email.service.sendgrid;

import email.web.api.model.EmailNotification;
import email.web.api.model.Response;
import lombok.Builder;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Builder
public class SendGridStaticAPI {
    public static Response api(EmailNotification emailNotification) {
        return Response.builder()
                .headers("Send Grid Email Service Provider - Sent")
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
