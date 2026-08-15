package com.example.user_service.controller;


import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Creation user test")
    void createUser_shouldReturnCreatedUser() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    @DisplayName("Get user by id test")
    void getUserById_shouldReturnUser() throws Exception {
        // given
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.getUserById(1L)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @DisplayName("Get all users test")
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        // given
        UserResponseDto user1 = UserResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponseDto user2 = UserResponseDto.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane@example.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        // when & then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Update user test")
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .age(35)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .name("Updated Name")
                .email("updated@example.com")
                .age(35)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.updateUser(eq(1L), any(UserRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @DisplayName("Delete user test")
    void deleteUser_shouldReturnNoContent() throws Exception {
        // given
        doNothing().when(userService).deleteUser(1L);

        // when & then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create user with empty name test")
    void createUser_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("")
                .email("john@example.com")
                .age(30)
                .build();

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create user with invalid email test")
    void createUser_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        // given
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("John Doe")
                .email("invalid-email")
                .age(30)
                .build();

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}