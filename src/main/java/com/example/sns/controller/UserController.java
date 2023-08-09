package com.example.sns.controller;

import com.example.sns.dto.ResponseDto;
import com.example.sns.dto.UserInfoDto;
import com.example.sns.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // 프로필 이미지 등록
    @PutMapping(value = "/profile/{username}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto uploadImage(@PathVariable("username") String username,
                                   @RequestParam MultipartFile image,
                                   Authentication authentication) {
        return userService.updateUserImage(username, image, authentication);
    }
    // 사용자 정보 조회
    @GetMapping("/profile/{username}")
    public UserInfoDto readUserProfile(@PathVariable("username")String username){
        return userService.readUserProfile(username);

    }
}
