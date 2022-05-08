package email.service;

import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import email.web.api.model.EmailNotification;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultEmailService implements EmailServiceProvider {

    @Override
    public boolean isApplicable(String provider) {
        return Arrays.stream(Provider.values())
                .collect(Collectors.toList()).stream()
                .filter(p -> p.name().equalsIgnoreCase(provider))
                .findFirst().isEmpty();
    }

    @Override
    public Response sendEmail(EmailNotification emailNotification) {
        return Response.builder()
                .headers("Default Email Service Provider - Sent")

                .build();
    }
}
