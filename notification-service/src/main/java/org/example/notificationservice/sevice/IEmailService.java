package org.example.notificationservice.sevice;

public interface IEmailService {
    void sendEmail(String to, String subject, String text);
}
