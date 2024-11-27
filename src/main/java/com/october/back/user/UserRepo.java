package com.october.back.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long id);
    Optional<UserEntity> findByNickName(String nickName);
}
