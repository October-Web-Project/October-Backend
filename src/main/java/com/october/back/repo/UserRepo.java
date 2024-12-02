package com.october.back.repo;

import com.october.back.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByNickName(String nickName);
    Optional<Users> findByEmail(String email);

    boolean existsByNickName(String nickName);

}
