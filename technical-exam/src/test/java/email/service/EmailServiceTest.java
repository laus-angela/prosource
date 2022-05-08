package email.service;

import email.exception.BadRequestException;
import email.exception.MailServicesOfflineException;
import email.gateway.MailGateway;
import email.gateway.MailRequest;
import email.service.mailgun.MailGunEmailService;
import email.service.sendgrid.SendGridEmailService;
import email.web.api.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import email.web.api.model.EmailNotification;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private MailGateway mailGateway;

    private EmailNotification emailNotification;
    private MailRequest mailRequest;

    @Mock
    private DefaultEmailService defaultEmailService;
    @Mock
    private MailGunEmailService mailGunEmailService;
    @Mock
    private SendGridEmailService sendGridEmailService;

    @BeforeEach
    void init() {
        emailNotification = EmailNotification.builder()
                .sender("123@host.com")
                .subjectLine("test email")
                .message("test mail message")
                .toRecipients(Collections.singletonList("456@host.com"))
                .build();
        mailRequest = MailRequest.builder()
                .from("123@host.com")
                .to(Collections.singletonList("456@host.com"))
                .subjectLine("test email")
                .message("test mail message")
                .build();
        defaultEmailService = new DefaultEmailService(mailGateway);
        mailGunEmailService = new MailGunEmailService(mailGateway);
        sendGridEmailService = new SendGridEmailService(mailGateway);

        List<EmailServiceProvider> emailServiceProviders = List.of(defaultEmailService, mailGunEmailService, sendGridEmailService);
        ReflectionTestUtils.setField(emailService, "emailServiceProviders", emailServiceProviders);
    }

    @Test
    void testValidations() {
        EmailNotification invalidEmailNotification = EmailNotification.builder()
                .sender("123host.com")
                .subjectLine("test email")
                .message("test mail message")
                .toRecipients(Collections.singletonList("456@host.com"))
                .build();
        assertThrows(BadRequestException.class, () -> emailService.send(invalidEmailNotification));
    }

    @Test
    void testSendNotification_mailGun() {
        when(mailGateway.sendRequest(mailRequest)).thenReturn(200);
        Response expectedResponse = emailService.sendToProvider(emailNotification, "mailgun");
        assertNotNull(expectedResponse);
        assertEquals("Mail Gun Email Service Provider - Sent", expectedResponse.getHeaders());
    }

    @Test
    void testSendNotification_sendGrid() {
        when(mailGateway.sendRequest(mailRequest)).thenReturn(200);
        Response expectedResponse = emailService.sendToProvider(emailNotification, "sendgrid");
        assertNotNull(expectedResponse);
        assertEquals("Send Grid Email Service Provider - Sent", expectedResponse.getHeaders());
    }

    @Test
    void testSendNotification_serviceOffline() {
        when(mailGateway.sendRequest(mailRequest)).thenReturn(null);
        assertThrows(MailServicesOfflineException.class, () -> emailService.sendToProvider(emailNotification, "mailgun"));
    }

    @Test
    void testSend_loopAllMailServices() {
        Response expectedResponse = emailService.send(emailNotification);
        assertNotNull(expectedResponse);
    }

    @Test
    void testSend_loopAllMailServices_allFail() {
        ReflectionTestUtils.setField(emailService, "emailServiceProviders", emptyList());
        assertThrows(MailServicesOfflineException.class, () -> emailService.send(emailNotification));
    }
}