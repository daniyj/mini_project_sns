package com.example.sns.service;

import com.example.sns.dto.FeedDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    public ResponseDto createFeed(String title,String content, Authentication authentication, List<MultipartFile> images) {

        log.info(authentication.getName());

        // 이미지 업로드 함수로 처리해서 저장

        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 등록되었습니다.");
        return response;

    }
}
