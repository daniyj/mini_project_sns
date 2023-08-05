package com.example.sns.service;

import com.example.sns.dto.FeedDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.Feed;
import com.example.sns.entity.FeedImages;
import com.example.sns.entity.User;
import com.example.sns.exception.exceptionCase.ImageUpdateException;
import com.example.sns.repository.FeedImagesRepository;
import com.example.sns.repository.FeedRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final FeedImagesRepository feedImagesRepository;
    private final UserRepository userRepository;
    public ResponseDto createFeed(String title,String content, Authentication authentication, List<MultipartFile> images) {

        // 유저 정보 받아오기
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        // 게시글 저장하기
        Feed feedEntity = Feed.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        feedRepository.save(feedEntity);
        log.info(feedEntity.getTitle());
        log.info(feedEntity.getContent());

        for (MultipartFile image : images) {
            String imageUrl = uploadImage(image,authentication.getName());

            // 이미지 정보 저장
            FeedImages feedImage = FeedImages.builder()
                    .feed(feedEntity)
                    .imageUrl(imageUrl)
                    .build();
            feedImagesRepository.save(feedImage);

        }
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 등록되었습니다.");
        return response;

    }

    private String uploadImage(MultipartFile image,String username) {

        // 폴더 만들기
        String itemDirPath = String.format("image/feed/%s/", username);
        // 폴더가 존재하지 않을 시
        if (!Files.exists(Path.of(itemDirPath))){
            try {
                Files.createDirectories(Path.of(itemDirPath));
            } catch (IOException e) {
                throw new ImageUpdateException();
            }
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;

        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length-1];
        String profileFilename = "image." + extension;

        String profilePath = itemDirPath + profileFilename;

        // MultipartFile 저장
        try{
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ImageUpdateException();
        }

//        userEntity.setProfileImgUrl(String.format("/static/%s/%s",username,profileFilename));

        return String.format("/static/%s/%s",username,profileFilename);
    }
}

