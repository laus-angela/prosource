package email.service;

import email.web.api.model.EmailNotification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import static org.springframework.util.ObjectUtils.isEmpty;

public abstract class AbstractEmailServiceProvider {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;
    private static final boolean SMTP_SSL_ENABLED = true;
    private static final boolean SMTP_AUTH = true;

    public void sendEmailToProvider(EmailNotification emailNotification) {

    }

    public void sendEmail(EmailNotification emailNotification) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", SMTP_HOST);
        prop.put("mail.smtp.port", SMTP_PORT);
        prop.put("mail.smtp.auth", SMTP_AUTH);
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("sender@mail.com", "test123");
                    }
                });

        prepareAndSendMessage(session, emailNotification);
    }

    private void prepareAndSendMessage(Session session, EmailNotification emailNotification) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(session.getProperty("userName")));
            setRecipients(message, emailNotification);
            message.setSubject("Testing Gmail SSL");
            message.setText("Dear Mail Crawler - Please do not spam my email!");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void setRecipients(Message message, EmailNotification emailNotification) {
//        parseAddress(Message.RecipientType.TO, emailNotification.getRecipients(), message);
//        parseAddress(Message.RecipientType.CC, emailNotification.getCcRecipients(), message);
//        parseAddress(Message.RecipientType.BCC, emailNotification.getBccRecipients(), message);
    }

    private void parseAddress(Message.RecipientType recipientType, String recipients, Message message) {
        try {
            if (!isEmpty(recipients)) {
                message.setRecipients(recipientType, InternetAddress.parse(recipients));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
