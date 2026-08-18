package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDto {
    private Long userId;
    private String email;
    private String operation; // "CREATED" или "DELETED"
    private String timestamp;
}