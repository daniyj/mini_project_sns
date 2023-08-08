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
    @PostMapping
    public ResponseDto createComment(@RequestBody CommentDto commentDto,
                                     @PathVariable("feedId") Long feedId,
                                     Authentication authentication) {
        return commentService.createComment(commentDto,feedId,authentication);
    }

}
