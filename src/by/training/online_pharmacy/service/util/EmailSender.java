package by.training.online_pharmacy.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by vladislav on 26.07.16.
 */
public class EmailSender {
    private String username;
    private String password;
    private Properties props;
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH_VAL = "true";
    private static final String MAIL_SMTP_STARTTLS_ENABLE_VAL = "true";
    private static final String MAIL_SMTP_HOST_VAL = "smtp.gmail.com";
    private static final String MAIL_SMTP_PORT_VAL = "587";
    private static final Logger logger = LogManager.getRootLogger();
    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put(MAIL_SMTP_AUTH, MAIL_SMTP_AUTH_VAL);
        props.put(MAIL_SMTP_STARTTLS_ENABLE, MAIL_SMTP_STARTTLS_ENABLE_VAL);
        props.put(MAIL_SMTP_HOST, MAIL_SMTP_HOST_VAL);
        props.put(MAIL_SMTP_PORT, MAIL_SMTP_PORT_VAL);
    }

    public void send(String subject, String text, String toEmail){
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);
            Runnable transferMailThread = () -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    logger.error("Something went wrong when trying to send email");
                }
            };
            new Thread(transferMailThread).start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
