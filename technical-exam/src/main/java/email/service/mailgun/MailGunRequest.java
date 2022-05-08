package email.service.mailgun;

import email.web.api.model.Request;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpMethod;

@Builder
@Value
public class MailGunRequest extends Request {
    HttpMethod method;
    String endpoint;
    String from;
    String to;
    String cc;
    String bcc;
    String subject;
    String text;
}
