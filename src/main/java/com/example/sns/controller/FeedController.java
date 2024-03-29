package com.example.sns.controller;

import com.example.sns.dto.FeedInfoDto;
import com.example.sns.dto.FeedListDto;
import com.example.sns.dto.FeedRequestDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.service.FeedService;
import com.example.sns.service.LikeService;
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
    private final LikeService likeService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto createFeed(@RequestParam(required = false) List<MultipartFile> images,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  Authentication authentication) throws IOException {
            return feedService.createFeed(title,content,authentication,images);
    }

    // 사용자가 작성한 피드 전체 조회
    // GET /feeds/{username}
    @GetMapping("/list/{username}")
    public List<FeedListDto> readAllFeeds(@PathVariable("username") String username) {
        return feedService.readAllFeeds(username);
    }
    // 진행중
    // 피드 단독 조회 - 제목, 내용, 이미지 url들, 댓글 목록, 좋아요
    // GET /feeds/{feedId}/readOne
    @GetMapping("/{feedId}")
    public FeedInfoDto readOneFeed(@PathVariable("feedId") Long feedId) {
        return feedService.readOneFeed(feedId);
    }

    // 피드 수정
    @PutMapping(value = "{feedId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto updateFeed(@PathVariable("feedId")Long feedId,
                                  @RequestParam(required = false) List<MultipartFile> images,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  Authentication authentication) throws IOException {
        return feedService.updateFeed(feedId,title,content,authentication,images);
    }

    // 피드 삭제
    // 실제 삭제가 아닌 삭제 기록을 남긴다.
    @PutMapping("/{feedId}/deleted")
    public ResponseDto deleteFeed(@PathVariable("feedId") Long feedId) {
        return feedService.deleteFeed(feedId);
    }
    // 좋아요 요청, 취소
    @PostMapping("/{feedId}/likes")
    public ResponseDto clickLike(@PathVariable("feedId") Long feedId,Authentication authentication) {
        return likeService.clickLike(feedId, authentication);
    }



}
