package email.web;

import email.web.api.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import email.service.EmailService;
import email.web.api.model.EmailNotification;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailNotificationController {
    private final EmailService emailService;

    @PostMapping (value = "/email/notification")
    public Response emailWithProvider(@RequestParam(required = false) String provider,
                                      @Valid @RequestBody(required = false) EmailNotification emailNotification) {
        return emailService.sendNotification(emailNotification, provider);
    }
}
