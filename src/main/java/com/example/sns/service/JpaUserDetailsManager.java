package com.example.sns.service;

import com.example.sns.entity.CustomUserDetails;
import com.example.sns.entity.User;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;


    @Override
    public void createUser(UserDetails user) {
        // 중복 유저 존재하는지 검사
        if(userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(((CustomUserDetails) user).newEntity());
            log.info("회원저장 완료");
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username));
        return CustomUserDetails.fromEntity(userEntity);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
