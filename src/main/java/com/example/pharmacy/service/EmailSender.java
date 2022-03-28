package com.example.pharmacy.service;

import javax.mail.MessagingException;

public interface EmailSender {

    void sendSimpleEmail(String toAdress, String subject, String message);

    void sendMimeEmail(String toAddress, String subject, String message) throws MessagingException;

    void sendWelcomeEmail(String email, String username) throws MessagingException;
}
