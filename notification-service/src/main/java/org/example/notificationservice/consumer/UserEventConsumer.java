package org.example.notificationservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.UserEventDto;
import org.example.notificationservice.sevice.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consume(String message){
        try{
            log.info("Получено сообщение из Kafka {}", message);

            UserEventDto event = objectMapper.readValue(message, UserEventDto.class);

            String subject;
            String text;

            if ("CREATED".equals(event.getOperation())){
                subject = "Аккаунт создан!";
                text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
            } else if ("DELETED".equals(event.getOperation())){
                subject = "Аккаунт удален";
                text = "Здравствуйте! Ваш аккаунт был удалён.";
            } else {
                log.warn("Неизвестная операция {}", event.getOperation());
                return;
            }

            emailService.sendEmail(event.getEmail(), subject, text);
            log.info("Email отправлен на {}", event.getEmail());
        } catch (Exception e){
            log.error("Ошибка обработки сообщения {}", e.getMessage());
        }
    }
}
