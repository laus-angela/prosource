package email.service.sendgrid;

import email.web.api.model.Request;
import email.web.api.model.Response;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class SendGrid {
    private String apiKey;
    private Request request;

    public static Response api(SendGrid sendGrid) {
        return Response.builder()
                .headers("Send Grid Email Service Provider - Sent")
                .body(sendGrid.request.getBody())
                .statusCode(HttpStatus.OK)
                .build();
    }
}
