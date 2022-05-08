package email.gateway;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class MailRequest {
    String apiKey;
    String uri;

    String from;
    List<String> to;
    List<String> cc;
    List<String> bcc;
    String subjectLine;
    String message;
}
