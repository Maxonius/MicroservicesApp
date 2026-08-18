package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import com.example.user_service.exception.EmailAlreadyExistsException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto){
        log.info("Creating user: {}", requestDto);

        if (userRepository.existsByEmail(requestDto.getEmail())){
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }

        User user = userMapper.toEntity(requestDto);
        User saved = userRepository.save(user);

        eventPublisher.publishUserCreated(saved.getId(), saved.getEmail());

        log.info("User created, id: {}", saved.getId());
        return userMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.debug("Finding user with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.debug("Finding users");

        return userRepository.findAll().stream().map(userMapper::toResponseDto).toList();
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        log.info("Updating user with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getEmail().equals(requestDto.getEmail())
        && userRepository.existsByEmail(requestDto.getEmail())){
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }

        userMapper.updateEntity(user, requestDto);
        User updated = userRepository.save(user);

        log.info("Updated user with id {}", updated.getId());
        return userMapper.toResponseDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        eventPublisher.publishUserDeleted(user.getId(), user.getEmail());

        userRepository.deleteById(id);
        log.info("User with id {} deleted", id);
    }
}
