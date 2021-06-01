package by.bsuir.service.implementations;

import by.bsuir.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService {

    private final String SENDER_EMAIL = "kolodkonikita20010508@gmail.com";

    private final JavaMailSender emailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String receiver, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(messageBody);
        emailSender.send(message);
    }
}
