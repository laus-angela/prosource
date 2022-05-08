package email.service;

import email.service.mailgun.MailGunEmailService;
import email.service.sendgrid.SendGridEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import email.web.api.model.EmailNotification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void init() {
        DefaultEmailService defaultEmailService = new DefaultEmailService();
        MailGunEmailService mailGunEmailService = new MailGunEmailService();
        SendGridEmailService sendGridEmailService = new SendGridEmailService();

        List<EmailServiceProvider> emailServiceProviders = List.of(defaultEmailService, mailGunEmailService, sendGridEmailService);
        ReflectionTestUtils.setField(emailService, "emailServiceProviders", emailServiceProviders);
    }

    @Test
    void testSendNotification() {
        EmailNotification emailNotification = EmailNotification.builder().build();
        assertNotNull(emailService.sendNotification(emailNotification, "mailgun"));
    }
}