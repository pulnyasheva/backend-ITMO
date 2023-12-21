package com.academy.fintech.origination.core.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String recipient, String subject, String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(recipient);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        mailSender.send(mailMessage);
    }

    public void sendEmailForStatus(String email, ApplicationStatus status) {
        String subject = "Результат заявки на кредит";
        String text = "Ваша заявка на кредит была " + status.name();
        sendEmail(email, subject, text);
    }
}
