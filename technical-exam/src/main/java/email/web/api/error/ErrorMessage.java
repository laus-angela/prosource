package email.web.api.error;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorMessage {
    String msg;
}
