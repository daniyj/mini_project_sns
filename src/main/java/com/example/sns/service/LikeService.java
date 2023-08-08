package com.example.sns.service;

import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.Feed;
import com.example.sns.entity.Like;
import com.example.sns.entity.User;
import com.example.sns.exception.exceptionCase.NoAuthUserException;
import com.example.sns.exception.exceptionCase.NotFoundFeedException;
import com.example.sns.exception.exceptionCase.NotFoundUsernameException;
import com.example.sns.repository.FeedRepository;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    public ResponseDto clickLike(Long feedId, Authentication authentication) {
        // 유저 정보 받아오기
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                ()-> new NotFoundUsernameException());
        // 피드 정보 받아오기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()->new NotFoundFeedException());
        // 자신의 피드는 좋아요 할 수 없다.
        if(feed.getUser()==user)
            throw new NoAuthUserException();

        Like like = null;
        List<Like> likeList = likeRepository.findAllByUserId(user.getId());
        for (Like likeOne : likeList) {
            if (likeOne.getFeed().getId().equals(feedId)) {
                like = likeOne;
                break;
            }
        }
        ResponseDto response = new ResponseDto();
        // 좋아요가 있는 경우 제거
        if (like != null) {
            likeRepository.delete(like);
            response.setMessage("좋아요를 취소하였습니다.");
        }
        // 좋아요가 없는 경우 생성
        else {
            like = Like.builder()
                    .user(user)
                    .feed(feed)
                    .build();
            likeRepository.save(like);
            response.setMessage("좋아요를 눌렀습니다.");
        }

        return response;
    }
}
