package com.lysenkova.ioc.testentities;

    public class UserService implements Runnable {
        private MailService mailService;
        private int number;

        public UserService() {
        }

        public void setMailService(MailService mailService) {
            this.mailService = mailService;
        }

        public MailService getMailService() {
            return mailService;
        }

        private int getUserCount() {
            return (int) (Math.random()*1000);
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public void init() {
            setNumber(3);
        }

        @Override
        public void run() {
            while(true) {
                int numberOfUsersInSystem = getUserCount();
                mailService.sendEmail("tech@project.com", "there are " + numberOfUsersInSystem + " users in System");
            }
        }

        @Override
        public String toString() {
            return "UserService{" +
                    "mailService=" + mailService +
                    '}';
        }
}
