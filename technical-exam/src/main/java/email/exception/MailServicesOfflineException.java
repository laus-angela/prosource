package email.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MailServicesOfflineException extends RuntimeException {
    public MailServicesOfflineException(String message) {
        super(message);
    }
}
