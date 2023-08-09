package com.example.sns.service;

import com.example.sns.auth.JwtTokenUtils;
import com.example.sns.dto.*;
import com.example.sns.entity.CustomUserDetails;
import com.example.sns.entity.User;
import com.example.sns.exception.exceptionCase.*;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

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
            throw new NotMatchedPasswordException();

        // 아이디 중복 검사
        if(manager.userExists(dto.getUsername()))
            throw new DuplicateUsernameException();

        // manager에 dto를 details형태로 변환하여 전달
        manager.createUser(CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build());
        ResponseDto response = new ResponseDto();
        response.setMessage("회원가입이 완료되었습니다.");

        return response;
    }

    public JwtTokenDto loginUser(LoginDto dto) {
        // 아이디 존재 검사
        if(!manager.userExists(dto.getUsername()))
            throw new NotFoundUsernameException();

        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());

        // 비밀번호 일치 검사
        if(!userDetails.getPassword().equals(dto.getPassword()))
            throw new NotMatchedPasswordException();

        JwtTokenDto jwtToken = new JwtTokenDto();
        jwtToken.setJwtToken(jwtTokenUtils.generateToken(userDetails));
        return jwtToken;
    }

    public ResponseDto updateUserImage(String username, MultipartFile image, Authentication authentication) {

        User userEntity = userRepository.findByUsername(username).orElseThrow(
                ()-> new NotFoundUsernameException());
        if(!username.equals(authentication.getName()))
            throw new NoAuthUserException();

        // 폴더 만들기
        String itemDirPath = String.format("image/userProfile/%s/", username);

        try {
            Files.createDirectories(Path.of(itemDirPath));
        } catch (IOException e) {
            throw new ImageUpdateException();
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null; //null이 아니어야함. (null이라면 예외발생)

        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length-1];
        String profileFilename = filenameSplit[0] + "_" + LocalDate.now() + "." + extension;

        String profilePath = itemDirPath + profileFilename;

        // MultipartFile 저장
        try{
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ImageUpdateException();
        }

        userEntity.setProfileImgUrl(String.format("/static/%s/%s",username,profileFilename));
        userRepository.save(userEntity);

        // 응답
        ResponseDto response = new ResponseDto();
        response.setMessage("이미지가 등록되었습니다.");

        return response;
    }

    public UserInfoDto readUserProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NotFoundUsernameException());
        UserInfoDto infoDto = new UserInfoDto();
        infoDto.setUsername(username);
        infoDto.setProfileImageUrl(user.getProfileImgUrl());
        return infoDto;
    }
}
