package com.malevdb.MailService;

public class MessageBean {
    private MailingAccountBean account;
    private String title;
    private String content;
    private String[] recipients;

    public MessageBean(MailingAccountBean account, String title, String content, String[] recipients) {
        this.account = account;
        this.title = title;
        this.content = content;
        this.recipients = recipients;
    }

    public MessageBean() { this(new MailingAccountBean(), "", "", null); }

    public void setAccount(MailingAccountBean account) {
        this.account = account;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecipients(String[] recipients) { this.recipients = recipients; }

    public MailingAccountBean getAccount() { return account; }

    public String getTitle() { return title; }

    public String  getContent() { return content; }

    public String[] getRecipients() { return recipients; }
}
