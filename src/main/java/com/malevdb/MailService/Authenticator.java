package com.malevdb.MailService;

import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class Authenticator extends javax.mail.Authenticator {
    private final Properties properties;
    public Authenticator(Properties properties) {
        super();
        this.properties = properties;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
    }
}
