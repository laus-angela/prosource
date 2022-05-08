package email.web.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import email.exception.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Builder
@Data
@AllArgsConstructor
public class EmailNotification {
    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    private static final String INVALID_FORMAT = "Invalid email address format: %s";

    public EmailNotification() {

    }

    String sender;
    List<String> toRecipients;
    List<String> ccRecipients;
    List<String> bccRecipients;
    String subjectLine;
    String message;

    @JsonIgnore
    public void validate() {
        validateEmailAddress(this.sender);
        Optional.ofNullable(toRecipients).ifPresentOrElse(
                recipients -> recipients.forEach(this::validateEmailAddress),
                () -> {
                    throw new BadRequestException("Request is missing recipients");
                });
        Optional.ofNullable(ccRecipients).ifPresent(recipients -> recipients.forEach(this::validateEmailAddress));
        Optional.ofNullable(bccRecipients).ifPresent(recipients -> recipients.forEach(this::validateEmailAddress));
    }

    void validateEmailAddress(String address) {
        final Matcher matcher = VALID_EMAIL_PATTERN.matcher(address);
        if (!matcher.find()) {
            throw new BadRequestException(String.format(INVALID_FORMAT, address));
        }
    }
}
