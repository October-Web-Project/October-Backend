package com.october.back.jwt;

import com.october.back.entity.Users;
import com.october.back.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;


    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        Users user = userRepo.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        return new UserDetailsImpl(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByNickName(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        return new UserDetailsImpl(user);
    }
}