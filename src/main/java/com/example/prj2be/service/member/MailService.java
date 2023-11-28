package com.example.prj2be.service.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sendermail;
    private static int num = 0;

    public static void createNumber() {
        num = (int)(Math.random() * (90000)) + 100000;
    }

    public SimpleMailMessage createMail (String mail) {
        createNumber();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setSubject("인증메일");
        simpleMailMessage.setText("인증번호 : " + num);
        simpleMailMessage.setFrom("gmleh1245@gmail.com");

        return simpleMailMessage;
    }

    public void senMail(String mail) {
        javaMailSender.send(createMail(mail));
    }

}
