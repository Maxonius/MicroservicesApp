package com.example.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @Email(message = "Неверный формат")
    @NotBlank(message = "Email не должен быть пустым")
    private String email;

    @Min(value = 0, message = "Возраст должен быть больше 0")
    @Max(value = 100, message = "Возраст должен быть меньше 100")
    private Integer age;
}
