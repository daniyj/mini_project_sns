package com.example.sns.service;

import com.example.sns.dto.CommentDto;
import com.example.sns.dto.FeedInfoDto;
import com.example.sns.dto.FeedListDto;
import com.example.sns.dto.ResponseDto;
import com.example.sns.entity.*;
import com.example.sns.exception.exceptionCase.ImageUpdateException;
import com.example.sns.exception.exceptionCase.NotFoundFeedException;
import com.example.sns.exception.exceptionCase.NotFoundUsernameException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.FeedImagesRepository;
import com.example.sns.repository.FeedRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final FeedImagesRepository feedImagesRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    public ResponseDto createFeed(String title,String content, Authentication authentication, List<MultipartFile> images) {

        // 유저 정보 받아오기
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                ()-> new NotFoundUsernameException());


        // 게시글 저장하기
        Feed feed = Feed.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        feedRepository.save(feed);

        if (images != null) {
            for (MultipartFile image : images) {
                String profileFilename = uploadImage(image,authentication.getName());
                String imageUrl = String.format("/static/feed/%d/%s",feed.getId(),profileFilename);

                // 이미지 정보 저장
                FeedImage feedImage = FeedImage.builder()
                        .feed(feed)
                        .imageUrl(imageUrl)
                        .build();
                feedImagesRepository.save(feedImage);
            }
        }
        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 등록되었습니다.");
        return response;

    }

    private String uploadImage(MultipartFile image,String username) {

        // 폴더 만들기
        String imageDirPath = String.format("image/feed/%s/", username);
        // 폴더가 존재하지 않을 시
        if (!Files.exists(Path.of(imageDirPath))){
            try {
                Files.createDirectories(Path.of(imageDirPath));
            } catch (IOException e) {
                throw new ImageUpdateException();
            }
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;

        String[] filenameSplit = originalFilename.split("\\.");
        String extension = filenameSplit[filenameSplit.length-1];
        String profileFilename = filenameSplit[0] + "_" + LocalDate.now() + "." + extension;

        String profilePath = imageDirPath + profileFilename;

        // MultipartFile 저장
        try{
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ImageUpdateException();
        }

//        userEntity.setProfileImgUrl(String.format("/static/%s/%s",username,profileFilename));

//        return String.format("/static/%s/%s",username,profileFilename);
        return profileFilename;
    }
    public List<FeedListDto> readAllFeeds(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NotFoundUsernameException());

        List<FeedListDto> feedLists = new ArrayList<>();
        for (Feed feed : feedRepository.findAllByUserId(user.getId())) {
            FeedListDto feedListDto = new FeedListDto();
            feedListDto.setUsername(user.getUsername());
            feedListDto.setTitle(feed.getTitle());
            feedListDto.setRepresentativeImageUrl(feedImagesRepository.findTopByFeedId(feed.getId())
                            .map(feedImage -> feedImage.getImageUrl())
                            .orElse("static/image/basis/feed.png"));
            feedLists.add(feedListDto);

        }
       log.info(user.getProfileImgUrl());

        return feedLists;
    }
    // TODO 피드 단독 조회시, 피드에 연관된 모든 정보가 포함되어야 한다.
    // TODO 이는 등록된 모든 이미지를 확인할 수 있는 각각의 URL과, 댓글 목록, 좋아요의 숫자를 포함한다.
    public FeedInfoDto readOneFeed(Long feedId) {

        Feed feedEntity = feedRepository.findById(feedId).orElseThrow(
                ()-> new NotFoundFeedException());

        FeedInfoDto feedInfoDto = new FeedInfoDto();
        feedInfoDto.setTitle(feedEntity.getTitle());
        feedInfoDto.setContent(feedEntity.getContent());

        // 댓글, 이미지 url들, 좋아요 수
        // TODO 댓글, 좋아요 수 보류(기능 아직 넣기 전이라)
        // TODO 문제 - ImageUrl이 불러와지지 않는다. 조회가 되지 않아 size도 0으로 뜬다.
        // 위의 메소드 readAllFeeds()에서는 정상적으로 조회가 되었는데 왜 안될까..??
        log.info("readOneFeed 실행중");
        List<String> feedImageUrls = new ArrayList<>();
        log.info("top="+feedImagesRepository.findTopByFeedId(feedEntity.getId()).toString());
        log.info("size="+feedImagesRepository.findAllByFeedId(feedEntity.getId()).size()+"");
        log.info("size="+feedEntity.getFeedImages().size()+"");
        Long feedID = feedEntity.getId();
        log.info("feedID="+feedID);
        // feedEntity.getFeedImages()로 조회해도
        // feedImagesRepository.findAllByFeedId(feedEntity.getId())로 조회해도 안된다.
        for (FeedImage feedImage : feedImagesRepository.findAllByFeed(feedEntity)) {
            String url = feedImage.getImageUrl();
            log.info("url=" + url);
            feedImageUrls.add(url);
        }
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : feedEntity.getComments()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setContent(comment.getContent());
            commentDto.setUsername(comment.getUser().getUsername());
            commentDtos.add(commentDto);
        }
        log.info("dto url size="+feedImageUrls.size());
        feedInfoDto.setComments(commentDtos);
        feedInfoDto.setFeedImageUrls(feedImageUrls);
        feedInfoDto.setLikesCount(Long.valueOf(feedEntity.getLikeFeeds().size()));

        return feedInfoDto;
    }

    public ResponseDto updateFeed(Long feedId,String title, String content, Authentication authentication, List<MultipartFile> images) {
        // 유저 정보 받아오기
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                ()-> new NotFoundUsernameException());
        // 피드 정보 받아오기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()->new NotFoundFeedException());

        // 게시글 업데이트
        feed.update(title, content);

        // 기존 이미지 삭제
        List<FeedImage> feedImages = feedImagesRepository.findAllByFeed(feed);
        String imageDirPath = String.format("image/feed/%s/", user.getUsername());
        String imageUrl = String.format("/static/feed/%d/",feed.getId());

        for (FeedImage feedImage : feedImages) {
            String fileName = feedImage.getImageUrl().replace(imageUrl, "");
            try {
                Path imagePath = Path.of(imageDirPath + fileName);
                boolean isDeleted = Files.deleteIfExists(imagePath);
                if (isDeleted)
                    log.info("Image deleted:"+imagePath);
                else
                    log.warn("Image not deleted: " + imagePath);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            feedImagesRepository.delete(feedImage);
        }

        // 새 이미지 업데이트
        if (images != null) {
            for (MultipartFile image : images) {
                String profileFilename = uploadImage(image,authentication.getName());
                String imageUrl2 = String.format("/static/feed/%d/%s",feed.getId(),profileFilename);
                // 이미지 정보 저장
                FeedImage feedImage = FeedImage.builder()
                        .feed(feed)
                        .imageUrl(imageUrl2)
                        .build();
                feedImagesRepository.save(feedImage);
            }
        }
        feedRepository.save(feed);

        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 수정되었습니다.");
        return response;
    }

    public ResponseDto deleteFeed(Long feedId) {
        // 피드 정보 받아오기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()->new NotFoundFeedException());
        // 삭제 시간 기록
        String time = LocalDateTime.now().toString().split("\\.")[0];
        feed.setDeletedAt(time);
        feedRepository.save(feed);

        // 피드에 삭제 정보 기록
        feed.update("삭제된 게시글","삭제된 게시글입니다.");

        // 기존 이미지 삭제
        List<FeedImage> feedImages = feedImagesRepository.findAllByFeed(feed);
        String imageDirPath = String.format("image/feed/%s/", feed.getUser().getUsername());
        String imageUrl = String.format("/static/feed/%d/",feed.getId());

        for (FeedImage feedImage : feedImages) {
            String fileName = feedImage.getImageUrl().replace(imageUrl, "");
            try {
                Path imagePath = Path.of(imageDirPath + fileName);
                boolean isDeleted = Files.deleteIfExists(imagePath);
                if (isDeleted)
                    log.info("Image deleted:"+imagePath);
                else
                    log.warn("Image not deleted: " + imagePath);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            feedImagesRepository.delete(feedImage);
        }

        ResponseDto response = new ResponseDto();
        response.setMessage("피드가 삭제되었습니다.");
        return response;
    }
}

