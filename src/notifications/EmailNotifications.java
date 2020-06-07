package notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotifications {
    private String sender;
    private String userName;
    private String password;
    private Properties properties;

    public EmailNotifications(String sender, String userName, String password, String host) {
        this.sender = sender;
        this.userName = userName;
        this.password = password;

        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "false");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
    }

    public void sendEmail(String to, String myMessage, String subject) throws MessagingException {
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(myMessage, "text/html; charset=utf-8");
        Transport.send(message);

    }
}
