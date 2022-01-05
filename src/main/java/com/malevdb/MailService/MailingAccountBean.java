package com.malevdb.MailService;

public class MailingAccountBean {
    private String id;
    private String login;

    public MailingAccountBean(String id, String login) {
        this.id = id;
        this.login = login;
    }

    public MailingAccountBean() {
        this("", "");
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
