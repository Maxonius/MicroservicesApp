package com.example.user_service.service;

import com.example.user_service.dto.UserEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "user-events";

    public void publishUserCreated(Long userId, String email) {
        UserEventDto event = UserEventDto.builder()
                .userId(userId)
                .email(email)
                .operation("CREATED")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
        sendEvent(event);
    }

    public void publishUserDeleted(Long userId, String email) {
        UserEventDto event = UserEventDto.builder()
                .userId(userId)
                .email(email)
                .operation("DELETED")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
        sendEvent(event);
    }

    private void sendEvent(UserEventDto event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);
            log.info("Событие отправлено в Kafka: {}", json);
        } catch (Exception e) {
            log.error("Ошибка отправки события в Kafka: {}", e.getMessage(), e);
        }
    }
}