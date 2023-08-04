package com.example.sns.service;

import com.example.sns.auth.JwtTokenUtils;
import com.example.sns.dto.JwtTokenDto;
import com.example.sns.dto.LoginDto;
import com.example.sns.dto.RegisterDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.CustomUserDetails;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtTokenUtils jwtTokenUtils;
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

    public JwtTokenDto loginUser(LoginDto dto) {
        // 아이디 존재 검사
        if(!manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());

        // 비밀번호 일치 검사
        if(!userDetails.getPassword().equals(dto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        JwtTokenDto jwtToken = new JwtTokenDto();
        jwtToken.setJwtToken(jwtTokenUtils.generateToken(userDetails));
        return jwtToken;

    }
}
