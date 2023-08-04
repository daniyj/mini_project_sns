package com.example.sns.controller;

import com.example.sns.dto.JwtTokenDto;
import com.example.sns.dto.LoginDto;
import com.example.sns.dto.RegisterDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user/auth")
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseDto signup(@Valid @RequestBody RegisterDto dto) {
        log.info(dto.toString());
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public JwtTokenDto login(@Valid @RequestBody LoginDto dto){

        return userService.loginUser(dto);
    }
}
