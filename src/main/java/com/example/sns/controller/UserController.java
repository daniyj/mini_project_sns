package com.example.sns.controller;

import com.example.sns.dto.ResponseDto;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping(value = "/profile/{username}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto uploadImage(@PathVariable("username") String username,
                                   @RequestParam MultipartFile image) {
        return userService.updateUserImage(username, image);
    }
}
