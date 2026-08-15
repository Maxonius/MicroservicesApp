package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}
