package com.example.sns.controller;

import com.example.sns.dto.FeedListDto;
import com.example.sns.dto.FeedRequestDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto createFeed(@RequestParam List<MultipartFile> images,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  Authentication authentication) {
        log.info("getname()="+authentication.getName()); //dani
        log.info("title"+title);
        log.info("content"+content);
        for (MultipartFile image : images) {
            log.info("filename="+image.getOriginalFilename());
        }
        return feedService.createFeed(title,content,authentication,images);
    }

    // 사용자가 작성한 피드 전체 조회
    // 반환값 변경해야함
    @GetMapping("/{username}")
    public List<FeedListDto> readAllFeeds(@PathVariable("username") String username) {
        return feedService.readAllFeeds(username);
    }
}
