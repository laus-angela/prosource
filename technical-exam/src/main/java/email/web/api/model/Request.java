package email.web.api.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Builder
public abstract class Request {
    private final HttpMethod method;
    private final String endpoint;
    private final String body;
}
