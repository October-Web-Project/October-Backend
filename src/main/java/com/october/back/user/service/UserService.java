package com.october.back.user.service;

import com.october.back.user.entity.UserRole;
import com.october.back.user.entity.Users;
import com.october.back.user.service.dto.UserRequestDto;
import com.october.back.user.service.dto.UserResponseDto;
import com.october.back.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserRequestDto userRequestDto) {
        Users users = new Users();
        users.setName(userRequestDto.getName());
        users.setEmail(userRequestDto.getEmail());
        users.setNickname(userRequestDto.getNickname());
        users.setUserRole(UserRole.USER);
        Users savedUsers = userRepository.save(users);
        return savedUsers.getId();
    }

    public UserResponseDto getUser(Long id) {
        Optional<Users> users = userRepository.findById(id);

        return users.get().convertResponseDto();
    }

    public Optional<Users> findByName(String name) {
        return userRepository.findByName(name);
    }
    @Transactional
    public void updateUser(Users user) {
        userRepository.save(user);
    }

}
