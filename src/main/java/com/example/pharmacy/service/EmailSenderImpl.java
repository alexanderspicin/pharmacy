package com.example.pharmacy.service;


import com.example.pharmacy.entity.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;


@Service
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    EmailSenderImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendSimpleEmail(String toAdress, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAdress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMimeEmail(String toAddress, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);
        Context context = new Context();
        context.setVariable("username", "Alex");
        String body = this.templateEngine.process("welcome",context);
        messageHelper.setText(body,true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendWelcomeEmail(String email, String username) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(email);
        messageHelper.setSubject("This is a welcome email for your!!");
        Context context = new Context();
        context.setVariable("username", username);
        String body = this.templateEngine.process("welcome",context);
        messageHelper.setText(body,true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendTrackEmail(Order order) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(order.getUser().getEmail());
        messageHelper.setSubject("Track message");
        Context context = new Context();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        context.setVariable("orderId", order.getId());
        context.setVariable("orderStatus", order.getStatus());
        context.setVariable("email", order.getUser().getEmail());
        context.setVariable("orderDate", order.getCreateTime().format(dateTimeFormatter));
        String body = this.templateEngine.process("orderTrackEmail",context);
        messageHelper.setText(body,true);
        javaMailSender.send(mimeMessage);
    }
}
