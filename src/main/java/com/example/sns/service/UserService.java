package com.example.sns.service;

import com.example.sns.dto.RegisterDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.CustomUserDetails;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public ResponseDto registerUser(RegisterDto dto){
        // 비밀번호 검사
        if(!dto.getPassword().equals(dto.getPasswordCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // 아이디 중복 검사
        if(manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // manager에 dto를 details형태로 변환하여 전달
        manager.createUser(CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build());
        ResponseDto response = new ResponseDto();
        response.setResponse("회원가입이 완료되었습니다.");

        return response;
    }
}
