package com.lysenkova.ioc.testentities;

public class PaymentService {
    private MailService mailService;
    private int maxAmount;

    public PaymentService() {
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    void pay(String from, String to, double account) {
        mailService.sendEmail("from", "withdrawal successful");
        mailService.sendEmail("to", "credit successful");
    }

}
