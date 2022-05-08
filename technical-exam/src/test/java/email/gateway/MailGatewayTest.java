package email.gateway;

import email.web.api.model.EmailNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MailGatewayTest {
    @InjectMocks
    private MailGateway mailGateway;

    @Test
    void testSendRequest() {
        MailRequest mailRequest = MailRequest.builder()
                .from("123@host.com")
                .to(Collections.singletonList("456@host.com"))
                .subjectLine("test email")
                .message("test mail message")
                .build();
        assertNotNull(mailGateway.sendRequest(mailRequest));
    }
}