package email.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Value
public class Response {
    HttpStatus statusCode;
    String sender;
    String subjectLine;
    String body;
    String recipients;
    String headers;
    boolean hasCc;
    boolean hasBcc;
}
