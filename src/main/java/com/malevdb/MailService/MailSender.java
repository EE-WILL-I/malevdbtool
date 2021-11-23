package com.malevdb.MailService;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.SpringConfigurations.MailServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailSender {
    private Session session;
    private InternetAddress addressFrom;
    private final List<InternetAddress> recipients = new ArrayList<>();
    private static AnnotationConfigApplicationContext mailServiceContext;

    @Autowired
    public MailSender(Properties properties) {
        try {
            session = Session.getDefaultInstance(properties, new Authenticator(properties));
            addressFrom = new InternetAddress(properties.getProperty("mail.smtp.from"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MailSender getInstance() {
        if(mailServiceContext == null)
            mailServiceContext = new AnnotationConfigApplicationContext(MailServiceConfiguration.class);
        return mailServiceContext.getBean(MailSender.class);
    }

    public void sendMessage(String content, String subject, String... recipients) {
        Logger.log(MailSender.class, "Sending message...", 4);
        addRecipients(recipients);
        MimeMessage message = prepareMessage(content, subject);
        send(message);

        StringBuilder recipientsString = new StringBuilder();
        for(String recipient : recipients)
            recipientsString.append(recipient);
        Logger.log(MailSender.class, "Message sent to: " + recipientsString.toString(), 1);
    }

    public void addRecipients(String[] recipients) {
        for(String recipient : recipients)
            addRecipient(recipient);
    }

    public void addRecipient(String address) {
        try {
            recipients.add(new InternetAddress(address));
        } catch (AddressException e) {
            Logger.log(MailSender.class, "Cannot add recipient " + address, 2);
            e.printStackTrace();
        }
    }

    public MimeMessage prepareMessage(String content, String subject) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(addressFrom);
            message.addRecipients(Message.RecipientType.TO, recipients.toArray(new InternetAddress[0]));
            message.setSubject(subject);
            message.setText(content);
            message.setSender(addressFrom);
            return message;
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    private void send(MimeMessage message) {
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            Logger.log(MailSender.class, "Cannot send message", 2);
            e.printStackTrace();
        }
    }
}
