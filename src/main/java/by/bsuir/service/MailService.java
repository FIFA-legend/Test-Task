package by.bsuir.service;

public interface MailService {

    void sendEmail(String receiver, String subject, String messageBody);

}
