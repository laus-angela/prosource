package email.service;


import email.web.api.model.EmailNotification;
import email.web.api.model.Response;

public interface EmailServiceProvider {
    boolean isApplicable(String provider);
    Response sendEmail(EmailNotification emailNotification);
}
