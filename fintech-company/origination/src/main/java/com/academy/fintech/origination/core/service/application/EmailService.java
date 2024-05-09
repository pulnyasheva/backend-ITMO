package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.agreement.CreationAgreementResult;
import lombok.RequiredArgsConstructor;
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

    public void sendEmailForStatus(String email, CreationAgreementResult result) {
        String subject = "Результат заявки на кредит";
        String text;
        if (result.created()) {
            text = "Ваша заявка на кредит была одобрена, номер вашего договора: " + result.agreementId();
        } else {
            text = "Ваша заявка на кредит была отклонена";
        }
        sendEmail(email, subject, text);
    }
}
