package com.lysenkova.ioc.testentities;


public class PaymentService {
    private MailServiceImpl mailService;
    private int maxAmount;

    public PaymentService() {
    }

    public MailServiceImpl getMailService() {
        return mailService;
    }

    public void setMailService(MailServiceImpl mailService) {
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


    @Override
    public String toString() {
        return "PaymentService{" +
                "mailService=" + mailService +
                ", maxAmount=" + maxAmount +
                '}';
    }
}
