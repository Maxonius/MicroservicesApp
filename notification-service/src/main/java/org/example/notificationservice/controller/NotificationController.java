package org.example.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.UserEventDto;
import org.example.notificationservice.sevice.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody UserEventDto event) {
        log.info("Получен запрос на отправку уведомления {}", event);

        String subject;
        String text;

        if ("CREATED".equals(event.getOperation())) {
            subject = "Аккаунт создан";
            text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        } else if ("DELETED".equals(event.getOperation())) {
            subject = "Аккаунт удален";
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return ResponseEntity.badRequest().body("Неизвестная операция " + event.getOperation());
        }
        emailService.sendEmail(event.getEmail(), subject, text);
        return ResponseEntity.ok("Уведомления отправлено на " + event.getEmail());
    }

}
