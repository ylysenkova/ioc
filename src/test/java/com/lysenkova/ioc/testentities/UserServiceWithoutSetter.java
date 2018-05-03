package com.lysenkova.ioc.testentities;

public class UserServiceWithoutSetter {
    private MailService mailService;

    public UserServiceWithoutSetter() {
    }

    public MailService getMailService() {
        return mailService;
    }
}
