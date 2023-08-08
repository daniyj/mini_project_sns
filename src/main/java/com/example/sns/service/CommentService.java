package com.example.sns.service;

import com.example.sns.dto.CommentDto;
import com.example.sns.dto.FeedRequestDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Feed;
import com.example.sns.entity.User;
import com.example.sns.exception.exceptionCase.NotFoundFeedException;
import com.example.sns.exception.exceptionCase.NotFoundUsernameException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.FeedRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public ResponseDto createComment(CommentDto commentDto, Long feedId, Authentication authentication) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new NotFoundFeedException());
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new NotFoundUsernameException());

        Comment comment = Comment.builder()
                .feed(feed)
                .user(user)
                .content(commentDto.getContent())
                .build();
        commentRepository.save(comment);

        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 등록되었습니다.");
        return response;
    }

}