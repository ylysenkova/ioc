package com.lysenkova.ioc.testentities;

public class UserServiceWithoutSetter {
    private MailServiceImpl mailService;

    public UserServiceWithoutSetter() {
    }

    public MailServiceImpl getMailService() {
        return mailService;
    }
}
