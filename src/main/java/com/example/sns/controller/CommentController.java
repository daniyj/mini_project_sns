package com.example.sns.controller;

import com.example.sns.dto.CommentDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.repository.CommentRepository;
import com.example.sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("feeds/{feedId}/comments")
public class CommentController {
    private final CommentService commentService;
    // 댓글 작성
    // POST /feeds/{feedId}/comments
    @PostMapping
    public ResponseDto createComment(@RequestBody CommentDto commentDto,
                                     @PathVariable("feedId") Long feedId,
                                     Authentication authentication) {
        return commentService.createComment(commentDto, feedId, authentication);
    }
    // 댓글 수정
    // PUT /feeds/{feedId}/comments/{commentId}
    @PutMapping("/{commentId}")
    public ResponseDto updateComment(@RequestBody CommentDto commentDto,
                                     @PathVariable("feedId") Long feedId,
                                     @PathVariable("commentId") Long commentId,
                                     Authentication authentication) {
        return commentService.updateComment(commentDto, feedId, commentId, authentication);
    }
    // 댓글 삭제
    // PUT /feeds/{feedId}/comments/{commentId}/deleted
    @PutMapping("/{commentId}/deleted")
    public ResponseDto deleteComment(@PathVariable("feedId") Long feedId,
                                     @PathVariable("commentId") Long commentId,
                                     Authentication authentication){
        return commentService.deleteComment(feedId, commentId, authentication);
    }
}
