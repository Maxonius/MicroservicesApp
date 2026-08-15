package com.example.user_service.controller;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя и возвращает его с HATEOAS-ссылками")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "409", description = "Email уже существует")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto){
        log.info("POST /api/users - create user");
        UserResponseDto user = userService.createUser(requestDto);
        addHateoasLinks(user);
        return user;
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя с HATEOAS-ссылками")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public UserResponseDto getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id){
        log.info("GET /api/users/{} - get user", id);
        UserResponseDto user = userService.getUserById(id);
        addHateoasLinks(user);
        return user;
    }

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей с HATEOAS-ссылками")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    })
    @GetMapping
    public List<UserResponseDto> getAllUsers(){
        log.info("GET api/users - get all users");
        List<UserResponseDto> users = userService.getAllUsers();
        users.forEach(this::addHateoasLinks);
        return users;
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя и возвращает его с HATEOAS-ссылками")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Email уже существует")
    })
    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id, @Valid @RequestBody UserRequestDto requestDto){
        log.info("PUT /api/users/{} - update user", id);
        UserResponseDto user = userService.updateUser(id, requestDto);
        addHateoasLinks(user);
        return user;
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id){
        log.info("DELETE /api/users/{} - delete user", id);
        userService.deleteUser(id);
    }


    private void addHateoasLinks(UserResponseDto user){
        if (user == null || user.getId() == null){
            return;
        }

        Link selfLink = linkTo(methodOn(UserController.class).getUserById(user.getId()))
                .withSelfRel()
                .withType("GET");
        user.add(selfLink);

        Link allUsersLink = linkTo(methodOn(UserController.class).getAllUsers())
                .withRel("all-users")
                .withType("GET");
        user.add(allUsersLink);

        Link updateLink = linkTo(methodOn(UserController.class).updateUser(user.getId(), null))
                .withRel("update")
                .withType("PUT");
        user.add(updateLink);

        Link deleteLink = Link.of(
                "/api/users/" + user.getId(),
                "delete")
                .withType("DELETE");
        user.add(deleteLink);

        Link createLink = linkTo(methodOn(UserController.class).createUser(null))
                .withRel("create")
                .withType("POST");
        user.add(createLink);
    }
}
