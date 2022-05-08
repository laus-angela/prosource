package email.gateway;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class MailGateway {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(java.net.http.HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    public Integer sendRequest(MailRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(buildJsonBody(request)))
                    .uri(URI.create("https://" + request.getUri()))
                    .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ioException) {
            ioException.getStackTrace();
        }
        return 200;
    }

    private String buildJsonBody(MailRequest request) {
        return "{" +
                "\"subject\":\"" + request.getSubjectLine() + "\"," +
                "\"from\":\"" + request.getFrom() + "\"," +
                "\"text\":\"" + request.getMessage() + "\"," +
                "\"to\":\"" + StringUtils.join(request.getTo(), ';') + "\"" +
                setCcBcc(request.getCc(), request.getBcc()) +
                "}";
    }

    private String setCcBcc(List<String> cc, List<String> bcc) {
        StringBuilder ccBccJsonString = new StringBuilder();
        if (Optional.ofNullable(cc).isPresent()) {
            ccBccJsonString.append("\"," + "\"cc\":\"").append(StringUtils.join(cc, ';'));
        }
        if (Optional.ofNullable(bcc).isPresent()) {
            ccBccJsonString.append("\"," + "\"bcc\":\"").append(StringUtils.join(bcc, ';'));
        }
        return ccBccJsonString.toString();
    }
}
