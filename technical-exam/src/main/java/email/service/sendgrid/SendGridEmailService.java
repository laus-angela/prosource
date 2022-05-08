package email.service.sendgrid;

import email.service.EmailServiceProvider;
import email.service.Provider;
import email.web.api.model.EmailNotification;
import email.web.api.model.Request;
import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendGridEmailService implements EmailServiceProvider {
    // TODO
//    private final RestTemplate restTemplate;

    @Value("${email-gateway.send-grid.api-key}")
    private String apiKey;

    @Value("${email-gateway.send-grid.url}")
    private String gatewayUrl;

//    @Builder
//    private static class SendGrid {
//        private String apiKey;
//        private Request request;

//        @Builder
//        private static class Request {
//            private HttpMethod method;
//            private String endpoint;
//            private String body;
//        }

//        public static Response api(SendGrid sendGrid) {
//            return Response.builder()
//                    .headers("Send Grid Email Service Provider - Sent")
//                    .body(sendGrid.request.getBody())
//                    .statusCode(HttpStatus.OK)
//                    .build();
//        }
//    }

    @Override
    public boolean isApplicable(String provider) {
        return provider.equalsIgnoreCase(Provider.SENDGRID.name());
    }

    @Override
    public Response sendEmail(EmailNotification emailNotification) {
        SendGrid sendGridRequest = SendGrid.builder()
                .apiKey(apiKey)
                .request(Request.builder()
                        .endpoint(gatewayUrl)
                        .method(HttpMethod.POST)
                        .body(emailNotification.getMessage())
                        .build())
                .build();

        return SendGrid.api(sendGridRequest);
    }
}
