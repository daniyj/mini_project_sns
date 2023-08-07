package com.example.sns.controller;

import com.example.sns.dto.FeedInfoDto;
import com.example.sns.dto.FeedListDto;
import com.example.sns.dto.FeedRequestDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto createFeed(@RequestParam(required = false) List<MultipartFile> images,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  Authentication authentication) throws IOException {
            return feedService.createFeed(title,content,authentication,images);
    }

    // 사용자가 작성한 피드 전체 조회
    @GetMapping("/{username}")
    public List<FeedListDto> readAllFeeds(@PathVariable("username") String username) {
        return feedService.readAllFeeds(username);
    }
    // 피드 단독 조회
    @GetMapping("/{feedId}/readOne")
    public FeedInfoDto readOneFeed(@PathVariable("feedId") Long feedId) {
        return feedService.readOneFeed(feedId);
    }
    // 제목, 내용, 이미지 url들, 댓글 목록, 좋아요
}
