package com.lysenkova.ioc.testentities;

import java.util.Objects;

public class MailServiceImpl implements MailService{
    private String protocol;
    private int port;

    public MailServiceImpl() {
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendEmail(String emailTo, String content) {
        System.out.println("Sending email to: " + emailTo);
        System.out.println("With content: " + content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailServiceImpl)) return false;
        MailServiceImpl that = (MailServiceImpl) o;
        return getPort() == that.getPort() &&
                Objects.equals(getProtocol(), that.getProtocol());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getProtocol(), getPort());
    }

    @Override
    public String toString() {
        return "MailService{" +
                "protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }
}
