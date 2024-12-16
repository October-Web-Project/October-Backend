package com.october.back.user.service;

import com.october.back.global.exception.UserException;
import com.october.back.user.entity.UserRole;
import com.october.back.user.entity.Users;
import com.october.back.user.service.dto.UserRequestDto;
import com.october.back.user.repository.UserRepository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.october.back.global.common.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserRequestDto userRequestDto) {
        Optional<Users> existingUser = userRepository.findByName(userRequestDto.getName());

        if (existingUser.isPresent()) {
            throw new UserException(ALREADY_EXIST_USER);
        }

        Users newUser = Users.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .nickname(userRequestDto.getNickname())
                .userRole(UserRole.USER)
                .build();
        Users savedUsers = userRepository.save(newUser);
        return savedUsers.getId();
    }

    public Optional<Users> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public void updateUser(Users user) {
        userRepository.save(user);
    }

}
