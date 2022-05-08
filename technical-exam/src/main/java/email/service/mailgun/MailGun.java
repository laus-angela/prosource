package email.service.mailgun;

import email.web.api.model.Request;
import email.web.api.model.Response;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class MailGun {
    private String apiKey;
    private MailGunRequest request;

    public static Response messages(MailGun mailGun) {
        return Response.builder()
                .headers("Mail Gun Email Service Provider - Sent")
                .subjectLine(mailGun.request.getSubject())
                .sender(mailGun.request.getFrom())
                .body(mailGun.request.getText())
                .recipients(mailGun.request.getTo())
                .statusCode(HttpStatus.OK)
                .build();
    }
}
