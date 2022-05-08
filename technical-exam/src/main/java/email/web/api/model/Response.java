package email.web.api.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Builder
@Value
public class Response {
    HttpStatus statusCode;
    String sender;
    String subjectLine;
    String body;
    String recipients;
    String headers;
}
