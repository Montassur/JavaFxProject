// src/main/java/org/example/services/MailService.java
package org.example.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailService {
    private final Session session;
    private final String from;

    /**
     * Constructs a MailService that uses Gmail SMTP.
     *
     * @param username    your full Gmail address (e.g. montassar121@gmail.com)
     * @param appPassword the App Password you generated in your Google Account
     */
    public MailService(String username, String appPassword) {
        this.from = username;

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        // trust Gmail's certificate
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });
    }

    /**
     * Sends a simple plain‐text email.
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email body text
     */
    public void sendSimple(String to, String subject, String body) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }

    /**
     * Sends your \"account created\" notification.
     *
     * @param to  recipient email address
     * @param nom recipient's name
     */
    public void sendAccountCreated(String to, String nom) throws MessagingException {
        String subject = "Votre compte a été créé";
        String body = "Bonjour " + nom + ",\n\n"
                + "Votre compte a bien été créé. Vous pouvez désormais vous connecter.\n\n"
                + "Cordialement.";
        sendSimple(to, subject, body);
    }
}
